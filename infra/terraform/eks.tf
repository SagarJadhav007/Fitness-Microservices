module "fitx_eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 21.0"

  name               = "fitx-eks"
  kubernetes_version = "1.36"

  endpoint_public_access  = true
  endpoint_private_access = true

  vpc_id = aws_vpc.fitx_vpc.id

  subnet_ids = [
    aws_subnet.fitx_private_subnet.id,
    aws_subnet.fitx_private_subnet_b.id
  ]

  enable_cluster_creator_admin_permissions = true

  addons = {
    vpc-cni = {
      most_recent    = true
      before_compute = true
    }

    kube-proxy = {
      most_recent    = true
      before_compute = true
    }

    coredns = {
      most_recent = true
    }
  }

  eks_managed_node_groups = {
    fitx_nodes = {
      name = "fitx-nodes"

      instance_types = ["t3.small"]

      min_size     = 1
      desired_size = 2
      max_size     = 2

      subnet_ids = [
        aws_subnet.fitx_private_subnet.id,
        aws_subnet.fitx_private_subnet_b.id
      ]
    }
  }

  tags = {
    Project = "fitx"
  }
}