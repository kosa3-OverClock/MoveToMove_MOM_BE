package com.kosa.kosafinalprojbackend.domains.member.oAuth.domain;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProviderTypeHandler extends BaseTypeHandler<ProviderType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ProviderType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public ProviderType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String socialType = rs.getString(columnName);
        return socialType != null ? ProviderType.valueOf(socialType) : null;
    }

    @Override
    public ProviderType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String socialType = rs.getString(columnIndex);
        return socialType != null ? ProviderType.valueOf(socialType) : null;
    }

    @Override
    public ProviderType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String socialType = cs.getString(columnIndex);
        return socialType != null ? ProviderType.valueOf(socialType) : null;
    }
}
