terraform {
  required_version = ">= 1.5"

  required_providers {
    neon = {
      source  = "kislerdm/neon"
      version = "0.18.0"
    }

    render = {
      source  = "render-oss/render"
      version = "1.9.1"
    }
  }
}

provider "neon" {}

provider "render" {}

resource "neon_project" "preprod" {
  name                      = "expense-tracker-preprod"
  org_id                    = var.neon_org_id
  history_retention_seconds = 21600
}

resource "neon_role" "preprod" {
  project_id = neon_project.preprod.id
  branch_id  = neon_project.preprod.default_branch_id
  name       = "app_preprod_owner"
}

resource "neon_database" "preprod" {
  project_id = neon_project.preprod.id
  branch_id  = neon_project.preprod.default_branch_id
  name       = "app_preprod"
  owner_name = neon_role.preprod.name
}

locals {
  jdbc_url = "jdbc:postgresql://${neon_project.preprod.database_host}:5432/${neon_database.preprod.name}?sslmode=require"
}

resource "render_web_service" "api" {
  name   = "expense-api-preprod"
  plan   = "free"
  region = var.region

  health_check_path = "/health"

  runtime_source = {
    image = {
      image_url = var.image_repo_api
      tag       = var.image_tag_api
    }
  }

  env_vars = {
    SPRING_DATASOURCE_URL = {
      value = local.jdbc_url
    }

    SPRING_DATASOURCE_USERNAME = {
      value = neon_role.preprod.name
    }

    SPRING_DATASOURCE_PASSWORD = {
      value = neon_role.preprod.password
    }
  }
}

resource "render_web_service" "front" {
  name   = "expense-front-preprod"
  plan   = "free"
  region = var.region

  runtime_source = {
    image = {
      image_url = var.image_repo_front
      tag       = var.image_tag_front
    }
  }

  env_vars = {
    BACKEND_URL = {
      value = trimsuffix(render_web_service.api.url, "/")
    }

    DNS_RESOLVER = {
      value = "8.8.8.8"
    }
  }
}
