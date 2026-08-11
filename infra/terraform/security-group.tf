resource "aws_security_group" "fitx_ci_sg" {
  name        = "fitx-ci-sg"
  description = "Security group for FitX CI server"
  vpc_id      = aws_vpc.fitx_vpc.id

  tags = {
    Name    = "fitx-ci-sg"
    Project = "fitx"
  }
}

resource "aws_vpc_security_group_ingress_rule" "ssh" {
  security_group_id = aws_security_group.fitx_ci_sg.id

  description = "SSH from local machine"
  cidr_ipv4   = var.my_ip
  from_port   = 22
  to_port     = 22
  ip_protocol = "tcp"
}

resource "aws_vpc_security_group_ingress_rule" "jenkins" {
  security_group_id = aws_security_group.fitx_ci_sg.id

  description = "Jenkins access"
  cidr_ipv4   = var.my_ip
  from_port   = 8080
  to_port     = 8080
  ip_protocol = "tcp"
}

resource "aws_vpc_security_group_ingress_rule" "sonarqube" {
  security_group_id = aws_security_group.fitx_ci_sg.id

  description = "SonarQube access"
  cidr_ipv4   = var.my_ip
  from_port   = 9000
  to_port     = 9000
  ip_protocol = "tcp"
}

resource "aws_vpc_security_group_egress_rule" "all_outbound" {
  security_group_id = aws_security_group.fitx_ci_sg.id

  description = "Allow all outbound IPv4 traffic"
  cidr_ipv4   = "0.0.0.0/0"
  ip_protocol = "-1"
}

// rabbit mq sg
resource "aws_security_group" "fitx_rabbitmq_sg" {
  name        = "fitx-rabbitmq-sg"
  description = "Security group for FitX Amazon MQ RabbitMQ"
  vpc_id      = aws_vpc.fitx_vpc.id

  tags = {
    Name    = "fitx-rabbitmq-sg"
    Project = "fitx"
  }
}

resource "aws_vpc_security_group_ingress_rule" "rabbitmq_from_ci" {
  security_group_id            = aws_security_group.fitx_rabbitmq_sg.id
  referenced_security_group_id = aws_security_group.fitx_ci_sg.id

  description = "RabbitMQ from FitX CI server"
  from_port   = 5671
  to_port     = 5671
  ip_protocol = "tcp"
}

resource "aws_vpc_security_group_egress_rule" "rabbitmq_all_outbound" {
  security_group_id = aws_security_group.fitx_rabbitmq_sg.id

  description = "Allow RabbitMQ outbound traffic"
  cidr_ipv4   = "0.0.0.0/0"
  ip_protocol = "-1"
}

resource "aws_vpc_security_group_ingress_rule" "rabbitmq_from_eks" {
  security_group_id = aws_security_group.fitx_rabbitmq_sg.id

  description = "RabbitMQ from EKS private subnets"
  cidr_ipv4   = "10.0.2.0/23"
  from_port   = 5671
  to_port     = 5671
  ip_protocol = "tcp"
}

resource "aws_security_group" "fitx_rds_sg" {
  name        = "fitx-rds-sg"
  description = "Security group for FitX PostgreSQL RDS"
  vpc_id      = aws_vpc.fitx_vpc.id

  tags = {
    Name    = "fitx-rds-sg"
    Project = "fitx"
  }
}

// rds sg
resource "aws_vpc_security_group_ingress_rule" "rds_from_ci" {
  security_group_id            = aws_security_group.fitx_rds_sg.id
  referenced_security_group_id = aws_security_group.fitx_ci_sg.id
  description                  = "PostgreSQL from FitX CI/application server"
  from_port                    = 5432
  to_port                      = 5432
  ip_protocol                  = "tcp"
}

resource "aws_vpc_security_group_ingress_rule" "rds_from_eks" {
  security_group_id = aws_security_group.fitx_rds_sg.id

  description = "PostgreSQL from EKS private subnets"
  cidr_ipv4   = "10.0.2.0/23"
  from_port   = 5432
  to_port     = 5432
  ip_protocol = "tcp"
}

resource "aws_vpc_security_group_egress_rule" "rds_all_outbound" {
  security_group_id = aws_security_group.fitx_rds_sg.id
  description       = "Allow RDS outbound traffic"
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}
