variable "region" { default = "ap-south-1" }
variable "project_name" { default = "my-api" }
variable "instance_type" { default = "t2.micro" }
variable "key_name" { default = "my_key" }       # your SSH key name
variable "my_ip_cidr" { default = "0.0.0.0/0" } # replace with your IP/CIDR  (e.g. "1.2.3.4/32")
variable "docker_image" { default = "bhoomikawandkar/my-api:1.0" }
variable "app_port" { default = 8090 }
variable "container_port" { default = 8090 }

variable "app_name" {
  description = "Logical name for the application"
  type        = string
  default     = "my-api"
}
