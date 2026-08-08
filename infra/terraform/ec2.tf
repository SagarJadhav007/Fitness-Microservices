data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd-gp3/ubuntu-noble-24.04-amd64-server-*"]
  }

  filter {
    name   = "architecture"
    values = ["x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  filter {
    name   = "root-device-type"
    values = ["ebs"]
  }
}

resource "aws_instance" "fitx_ci_server" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "c7i-flex.large"

  subnet_id                   = aws_subnet.fitx_public_subnet.id
  vpc_security_group_ids      = [aws_security_group.fitx_ci_sg.id]
  associate_public_ip_address = true

  key_name = aws_key_pair.fitx_key.key_name

  root_block_device {
    volume_size = 20
    volume_type = "gp3"

    encrypted = true
  }

  tags = {
    Name    = "fitx-ci-server"
    Project = "fitx"
  }
}