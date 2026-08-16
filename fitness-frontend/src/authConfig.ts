import type {
  TAuthConfig,
  TRefreshTokenExpiredEvent
} from "react-oauth2-code-pkce";

const KEYCLOAK_URL =
    "https://accessing-somehow-doctrine-comm.trycloudflare.com";

export const authConfig: TAuthConfig = {
  clientId: "fitx-frontend",

  authorizationEndpoint:
      `${KEYCLOAK_URL}/realms/fitness-oauth2/protocol/openid-connect/auth`,

  tokenEndpoint:
      `${KEYCLOAK_URL}/realms/fitness-oauth2/protocol/openid-connect/token`,

  redirectUri:
      "https://fitx-frontend-020641930163.s3.ap-south-1.amazonaws.com/index.html",

  scope: "openid profile email offline_access",

  onRefreshTokenExpire:
      (event: TRefreshTokenExpiredEvent) => event.logIn(),
};