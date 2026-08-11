terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.55"
    }
  }
}

# Configure the AWS Provider
provider "aws" {
  region = "ap-south-1"
}