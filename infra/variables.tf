variable "neon_org_id" {
  description = "ID de la organización de Neon"
  type        = string
}

variable "image_repo_api" {
  default = "ghcr.io/euge7777/ingsoft3-tp01-backend"
}

variable "image_repo_front" {
  default = "ghcr.io/euge7777/ingsoft3-tp01-frontend"
}

variable "image_tag_api" {
  default = "preprod-4f4668f"
}

variable "image_tag_front" {
  default = "sha-045c84a7494d4b479d7a6df6a88488cff8ec9b46"
}

variable "region" {
  default = "oregon"
}

