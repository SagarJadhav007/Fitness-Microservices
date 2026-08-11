resource "aws_db_subnet_group" "fitx_rds" {
  name        = "fitx-rds-subnet-group"
  description = "Private subnet group for FitX PostgreSQL"

  subnet_ids = [
    aws_subnet.fitx_private_subnet.id,
    aws_subnet.fitx_private_subnet_b.id
  ]

  tags = {
    Name    = "fitx-rds-subnet-group"
    Project = "fitx"
  }
}

resource "aws_db_instance" "fitx_postgres" {
  identifier = "fitx-postgres"

  engine         = "postgres"
  engine_version = "17"

  instance_class = "db.t3.micro"

  allocated_storage     = 20
  max_allocated_storage = 50
  storage_type          = "gp3"
  storage_encrypted     = true

  db_name  = "fitxdb"
  username = var.rds_username
  password = var.rds_password
  port     = 5432

  db_subnet_group_name   = aws_db_subnet_group.fitx_rds.name
  vpc_security_group_ids = [aws_security_group.fitx_rds_sg.id]

  publicly_accessible = false
  multi_az            = false

  backup_retention_period = 1
  deletion_protection     = false
  skip_final_snapshot     = true

  tags = {
    Name    = "fitx-postgres"
    Project = "fitx"
  }
}