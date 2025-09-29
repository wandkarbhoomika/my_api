# terraform.tfvars

region        = "ap-south-1"
project_name  = "insurance-api"
instance_type = "t2.micro"
key_name      = "my_key"            # If you don't want SSH set to empty ""
my_ip_cidr    = "0.0.0.0/0"         # Replace with "YOUR_PUBLIC_IP/32" for safer SSH
docker_image  = "bhoomikawandkar/my-api:latest"