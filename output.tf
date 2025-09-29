output "ec2_public_ip" {
  description = "Public IP of EC2 instance"
  value       = aws_instance.app.public_ip
}

output "s3_bucket_name" {
  description = "S3 bucket for quote logs"
  value       = aws_s3_bucket.quote_logs.bucket
}
