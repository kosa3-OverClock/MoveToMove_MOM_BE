package com.kosa.kosafinalprojbackend.domains.folder.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class JsonArrayTypeHandler extends BaseTypeHandler<List<Map<String, Object>>> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<Map<String, Object>> parameter,
      JdbcType jdbcType) throws SQLException {
    try {
      ps.setString(i, objectMapper.writeValueAsString(parameter));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Map<String, Object>> getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    String json = rs.getString(columnName);
    try {
      return json == null ? null
          : objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Map<String, Object>> getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    String json = rs.getString(columnIndex);
    try {
      return json == null ? null
          : objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Map<String, Object>> getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    String json = cs.getString(columnIndex);
    try {
      return json == null ? null
          : objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}