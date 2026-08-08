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