output "ci_server_public_ip" {
  description = "Public IP of the FitX CI server"
  value       = aws_instance.fitx_ci_server.public_ip
}

output "jenkins_url" {
  description = "Jenkins URL"
  value       = "http://${aws_instance.fitx_ci_server.public_ip}:8080"
}

output "sonarqube_url" {
  description = "SonarQube URL"
  value       = "http://${aws_instance.fitx_ci_server.public_ip}:9000"
}