locals {
  frontend_bucket_name = "fitx-frontend-020641930163"
}

# =========================================================
# S3 BUCKET
# =========================================================

resource "aws_s3_bucket" "frontend" {
  bucket = local.frontend_bucket_name
}

resource "aws_s3_bucket_ownership_controls" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  rule {
    object_ownership = "BucketOwnerEnforced"
  }
}

resource "aws_s3_bucket_versioning" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  versioning_configuration {
    status = "Enabled"
  }
}

# =========================================================
# S3 STATIC WEBSITE
# =========================================================

resource "aws_s3_bucket_website_configuration" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "index.html"
  }
}

# =========================================================
# PUBLIC ACCESS
# =========================================================

resource "aws_s3_bucket_public_access_block" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# =========================================================
# S3 PUBLIC READ
# =========================================================

data "aws_iam_policy_document" "frontend_bucket_policy" {
  statement {
    sid    = "PublicReadFrontend"
    effect = "Allow"

    principals {
      type        = "*"
      identifiers = ["*"]
    }

    actions = [
      "s3:GetObject"
    ]

    resources = [
      "${aws_s3_bucket.frontend.arn}/*"
    ]
  }
}

resource "aws_s3_bucket_policy" "frontend" {
  bucket = aws_s3_bucket.frontend.id
  policy = data.aws_iam_policy_document.frontend_bucket_policy.json
}

# =========================================================
# CLOUDFRONT
# =========================================================

resource "aws_cloudfront_distribution" "frontend" {

  enabled = true

  comment = "FitX Frontend"

  default_root_object = "index.html"

  # -------------------------------------------------------
  # S3 WEBSITE ORIGIN
  # -------------------------------------------------------

  origin {
    domain_name = aws_s3_bucket_website_configuration.frontend.website_endpoint
    origin_id   = "fitx-s3-website"

    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "http-only"

      origin_ssl_protocols = [
        "TLSv1.2"
      ]
    }
  }

  # -------------------------------------------------------
  # DEFAULT CACHE BEHAVIOR
  # -------------------------------------------------------

  default_cache_behavior {

    target_origin_id = "fitx-s3-website"

    viewer_protocol_policy = "redirect-to-https"

    allowed_methods = [
      "GET",
      "HEAD",
      "OPTIONS"
    ]

    cached_methods = [
      "GET",
      "HEAD"
    ]

    forwarded_values {
      query_string = true

      cookies {
        forward = "all"
      }
    }
  }

  # -------------------------------------------------------
  # SPA ROUTING
  # -------------------------------------------------------

  custom_error_response {
    error_code         = 403
    response_code      = 200
    response_page_path = "/index.html"
  }

  custom_error_response {
    error_code         = 404
    response_code      = 200
    response_page_path = "/index.html"
  }

  # -------------------------------------------------------
  # CLOUDFRONT CERTIFICATE
  # -------------------------------------------------------

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  # -------------------------------------------------------
  # PRICE
  # -------------------------------------------------------

  price_class = "PriceClass_100"

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  tags = {
    Project = "fitx"
    Name    = "fitx-frontend"
  }
}

# =========================================================
# OUTPUTS
# =========================================================

output "frontend_s3_bucket" {
  value = aws_s3_bucket.frontend.bucket
}

output "frontend_website_endpoint" {
  value = aws_s3_bucket_website_configuration.frontend.website_endpoint
}

output "frontend_cloudfront_domain" {
  value = aws_cloudfront_distribution.frontend.domain_name
}

output "frontend_cloudfront_distribution_id" {
  value = aws_cloudfront_distribution.frontend.id
}

output "frontend_url" {
  value = "https://${aws_cloudfront_distribution.frontend.domain_name}"
}