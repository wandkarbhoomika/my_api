# Terraform block to define providers
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"   # use latest 5.x
    }
    random = { source = "hashicorp/random", version = "~> 3.0" }
  }
}

# AWS provider and region
provider "aws" {
  region = var.region
}

# -----------------------------
# Random ID for unique S3 bucket
# -----------------------------
resource "random_id" "suffix" {
  byte_length = 4
}

# -----------------------------
# Get latest Amazon Linux 2 AMI via SSM
# -----------------------------
data "aws_ssm_parameter" "amzn2" {
  name = "/aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-x86_64-gp2"
}

# -----------------------------
# S3 Bucket for storing quote logs
resource "aws_s3_bucket" "quote_logs" {
  bucket        = "${var.project_name}-quote-logs-${random_id.suffix.hex}"
  force_destroy = true
}



# Separate resource for server-side encryption (recommended)
resource "aws_s3_bucket_server_side_encryption_configuration" "quote_logs_sse" {
  bucket = aws_s3_bucket.quote_logs.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

# -----------------------------
# IAM Role for EC2 instance
# -----------------------------
# Assume role policy: allows EC2 to assume this role
data "aws_iam_policy_document" "assume_role" {
  statement {
    effect = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ec2_role" {
  name               = "${var.project_name}-ec2-role"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}

# -----------------------------
# Scoped IAM policy to access only our S3 bucket
# -----------------------------
data "aws_iam_policy_document" "s3_policy" {
  statement {
    actions   = ["s3:ListBucket"]
    resources = [aws_s3_bucket.quote_logs.arn]
  }
  statement {
    actions   = ["s3:GetObject","s3:PutObject","s3:DeleteObject"]
    resources = ["${aws_s3_bucket.quote_logs.arn}/*"]
  }
}

resource "aws_iam_policy" "s3_policy" {
  name   = "${var.project_name}-s3-policy"
  policy = data.aws_iam_policy_document.s3_policy.json
}

# Attach policy to EC2 role
resource "aws_iam_role_policy_attachment" "attach_s3" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = aws_iam_policy.s3_policy.arn
}

# Instance profile to attach role to EC2
resource "aws_iam_instance_profile" "ec2_profile" {
  name = "${var.project_name}-instance-profile"
  role = aws_iam_role.ec2_role.name
}

# -----------------------------
# Security group for EC2
# -----------------------------
resource "aws_security_group" "sg" {
  name        = "${var.project_name}-sg"
  description = "Allow app port and SSH"

  # Allow HTTP/APP port
  ingress {
    from_port   = var.app_port
    to_port     = var.app_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow SSH only from your IP
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.my_ip_cidr]
  }

  # Allow all outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# -----------------------------
# EC2 Instance running Docker + API
# -----------------------------
resource "aws_instance" "app" {
  ami                         = data.aws_ssm_parameter.amzn2.value
  instance_type               = var.instance_type
  key_name                    = var.key_name
  iam_instance_profile        = aws_iam_instance_profile.ec2_profile.name
  vpc_security_group_ids      = [aws_security_group.sg.id]
  associate_public_ip_address = true

  # Enforce IMDSv2 for security
  metadata_options {
    http_tokens = "required"
  }

  # User data script to install Docker & run your API container
  user_data = <<-EOF
    #!/bin/bash
    set -xe
    yum update -y
    amazon-linux-extras install docker -y || yum install -y docker
    systemctl enable docker
    systemctl start docker
    usermod -a -G docker ec2-user

    docker pull ${var.docker_image} || true
    docker rm -f my-api || true
    docker run -d --restart=unless-stopped -p ${var.app_port}:${var.container_port} \
      -e BUCKET_NAME=${aws_s3_bucket.quote_logs.bucket} \
      --name my-api ${var.docker_image}
  EOF

  tags = { Name = "${var.app_name}-instance" }
}
