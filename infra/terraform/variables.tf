variable "my_ip" {
  description = "Public IP allowed to access FitX CI server"
  type        = string
}

variable "rabbitmq_username" {
  description = "Amazon MQ RabbitMQ username"
  type        = string
  sensitive   = true
}

variable "rabbitmq_password" {
  description = "Amazon MQ RabbitMQ password"
  type        = string
  sensitive   = true
}

variable "rds_username" {
  description = "RDS PostgreSQL master username"
  type        = string
  sensitive   = true
}

variable "rds_password" {
  description = "RDS PostgreSQL master password"
  type        = string
  sensitive   = true
}