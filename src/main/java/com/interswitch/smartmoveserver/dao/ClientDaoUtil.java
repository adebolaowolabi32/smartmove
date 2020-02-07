package com.interswitch.smartmoveserver.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitchng.passport.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jideobi.Onuora on 1/16/2015.
 */
public class ClientDaoUtil implements RowMapper<ClientDetails> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client details = new Client(rs.getString("client_name"),
                                    rs.getString("client_id"),
                                    rs.getString("client_secret_enc"),
                                    rs.getString("resource_ids"),
                                    rs.getString("scope"),
                                    rs.getString("authorized_grant_types"),
                                    rs.getString("authorities"),
                                    rs.getString("web_server_redirect_uri"),
                                    rs.getBoolean("mobile_no_required"),
                                    rs.getBoolean("email_required"),
                                    rs.getBoolean("authentication_email_required"),
                                    rs.getString("theme_name"),
                                    rs.getInt("link_validity"));
        details.setClientSecretEnc(rs.getString("client_secret_enc"));
        details.setClientSecret(rs.getString("client_secret"));
        if (rs.getObject("access_token_validity") != null) {
            details.setAccessTokenValiditySeconds(rs.getInt("access_token_validity"));
        }
        if (rs.getObject("refresh_token_validity") != null) {
            details.setRefreshTokenValiditySeconds(rs.getInt("refresh_token_validity"));
        }
        if (rs.getObject("client_owner") != null) {
            details.setClientOwner(rs.getString("client_owner"));
        }
        String json = rs.getString("additional_information");
        if (json != null) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> additionalInformation = mapper.readValue(json, Map.class);
                details.setAdditionalInformation(additionalInformation);
            } catch (Exception e) {
                logger.warn("Could not decode JSON for additional information: " + details, e);
            }
        }
        String scopes = rs.getString("autoapprove");
        if (scopes != null) {
            details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
        }
        return details;
    }

    public MapSqlParameterSource getClientPropertyMapSqlParameterSource(Client client) {
        MapSqlParameterSource params = getClientPropertyMapSqlParameterSourceForUpdate(client);

        String clientName = client.getClientName();
        String clientSecret = client.getClientSecret();
        String clientSecretEnc = client.getClientSecretEnc();
        String clientOwner = client.getClientOwner();

        params.addValue("client_name", clientName)
                .addValue("client_secret", clientSecret)
                .addValue("client_secret_enc", clientSecretEnc)
                .addValue("client_owner", clientOwner);

        return params;
    }

    public MapSqlParameterSource getClientPropertyMapSqlParameterSourceForUpdate(Client client) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String clientId = client.getClientId();
        String client_secret_enc = client.getClientSecretEnc();
        String resourceIds = client.getResourceIds() != null ?
                StringUtils.collectionToCommaDelimitedString(client.getResourceIds()) :
                null;
        String scope = client.getScope() != null ?
                StringUtils.collectionToCommaDelimitedString(client.getScope()) :
                null;
        String grantTypes = client.getAuthorizedGrantTypes() != null ?
                StringUtils
                        .collectionToCommaDelimitedString(client.getAuthorizedGrantTypes()) :
                null;
        String redirectUri = client.getRegisteredRedirectUri() != null ?
                StringUtils.collectionToCommaDelimitedString(client.getRegisteredRedirectUri()) :
                null;
        String authorities = client.getAuthorities() != null ?
                StringUtils.collectionToCommaDelimitedString(client.getAuthorities()) :
                null;
        Integer accessTokenValidity = client.getAccessTokenValiditySeconds();
        Integer refreshTokenValidity = client.getRefreshTokenValiditySeconds();
        String additionalInformation = null;
        try {
            additionalInformation = mapper.writeValueAsString(client.getAdditionalInformation());
        } catch (JsonProcessingException e) {
            logger.warn("Could not serialize additional information: " + client, e);
        }
        String autoapprove = getAutoApproveScopes(client);
        Boolean mobileNoRequired = client.getMobileNoRequired();
        Boolean emailRequired = client.getEmailRequired();
        String themeName = client.getThemeName();

        params.addValue("client_id", clientId)
                .addValue("client_secret_enc", client_secret_enc)
                .addValue("resource_ids", resourceIds)
                .addValue("scope", scope)
                .addValue("authorized_grant_types", grantTypes)
                .addValue("web_server_redirect_uri", redirectUri)
                .addValue("authorities", authorities)
                .addValue("access_token_validity", accessTokenValidity)
                .addValue("refresh_token_validity", refreshTokenValidity)
                .addValue("additional_information", additionalInformation)
                .addValue("autoapprove", autoapprove)
                .addValue("mobile_no_required", mobileNoRequired)
                .addValue("email_required", emailRequired)
                .addValue("authentication_email_required", client.getAuthenticationEmailRequired())
                .addValue("theme_name", themeName)
                .addValue("link_validity", client.getLinkValiditySeconds());

        return params;
    }

    private String getAutoApproveScopes(Client client) {
        if (client.isAutoApprove("true")) {
            return "true"; // all scopes autoapproved
        }
        Set<String> scopes = new HashSet<>();
        for (String scope : client.getScope()) {
            if (client.isAutoApprove(scope)) {
                scopes.add(scope);
            }
        }
        return StringUtils.collectionToCommaDelimitedString(scopes);
    }
}
