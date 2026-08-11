resource "aws_mq_broker" "fitx_rabbitmq" {
  broker_name                = "fitx-rabbitmq"
  engine_type                = "RabbitMQ"
  engine_version             = "3.13"
  auto_minor_version_upgrade = true

  host_instance_type  = "mq.m7g.medium"
  deployment_mode     = "SINGLE_INSTANCE"
  publicly_accessible = false

  subnet_ids = [
    aws_subnet.fitx_private_subnet.id
  ]

  security_groups = [
    aws_security_group.fitx_rabbitmq_sg.id
  ]

  user {
    username = var.rabbitmq_username
    password = var.rabbitmq_password
  }

  logs {
    general = true
  }

  tags = {
    Name    = "fitx-rabbitmq"
    Project = "fitx"
  }
}