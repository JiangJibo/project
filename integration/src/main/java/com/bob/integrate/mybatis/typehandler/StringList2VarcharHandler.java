package com.bob.integrate.mybatis.typehandler;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * List<String>转换为Varchar的类型处理器
 *
 * @author Administrator
 * @create 2018-04-01 19:10
 */
public class StringList2VarcharHandler extends BaseTypeHandler<List<String>> {

    private static final Gson GSON = new Gson();
    private static final Type GENERIC_TYPE = new TypeToken<ArrayList<String>>() {}.getType();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, GSON.toJson(parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return GSON.fromJson(rs.getString(columnName), GENERIC_TYPE);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return GSON.fromJson(rs.getString(columnIndex), GENERIC_TYPE);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return GSON.fromJson(cs.getString(columnIndex), GENERIC_TYPE);
    }
}
