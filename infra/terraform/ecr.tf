locals {
  fitx_services = [
    "activityservice",
    "aiservice",
    "analyticsservice",
    "configserver",
    "eureka",
    "gateway",
    "userservice"
  ]
}

resource "aws_ecr_repository" "fitx_services" {
  for_each = toset(local.fitx_services)

  name                 = "fitx/${each.key}"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Project = "fitx"
    Service = each.key
  }
}