variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "app_name" {
  description = "Application name"
  type        = string
  default     = "franchise-api"
}

variable "db_name" {
  description = "Database name"
  type        = string
  default     = "franchise_db"
}

variable "db_username" {
  description = "Database username"
  type        = string
  default     = "admin"
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

variable "ecr_image_uri" {
  description = "ECR image URI"
  type        = string
}