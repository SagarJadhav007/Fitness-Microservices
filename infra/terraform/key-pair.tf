resource "aws_key_pair" "fitx_key" {
  key_name   = "fitx-ci-key"
  public_key = file(pathexpand("~/.ssh/id_ed25519.pub"))

  tags = {
    Project = "fitx"
  }
}