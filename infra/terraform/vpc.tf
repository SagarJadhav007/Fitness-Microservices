resource "aws_vpc" "fitx_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name    = "fitx-vpc"
    Project = "fitx"
  }
}

resource "aws_subnet" "fitx_public_subnet" {
  vpc_id                  = aws_vpc.fitx_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "ap-south-1a"
  map_public_ip_on_launch = true

  tags = {
    Name    = "fitx-public-subnet"
    Project = "fitx"
  }
}

resource "aws_internet_gateway" "fitx_igw" {
  vpc_id = aws_vpc.fitx_vpc.id

  tags = {
    Name    = "fitx-igw"
    Project = "fitx"
  }
}

resource "aws_route_table_association" "fitx_public_rta" {
  subnet_id      = aws_subnet.fitx_public_subnet.id
  route_table_id = aws_route_table.fitx_public_rt.id
}

resource "aws_route_table" "fitx_public_rt" {
  vpc_id = aws_vpc.fitx_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.fitx_igw.id
  }

  tags = {
    Name    = "fitx-public-rt"
    Project = "fitx"
  }
}