/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.briefdb;


/**
 *
 * @author AutoPecasLuz
 */
    

/**
 *
 * @author Wellington Soares da Luz
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Classe java para simplificar o processo de consulta e crudo na banco de dados postgresql
 */
/** 
 * update 2017-10-25
 * @author Wellington Soares da Luz
 * @version 1.0
 */
public class BriefDb {

    public static final int INSERT = 1, UPDATE = 2, DELETE = 3;
    
    public static boolean close = true;
    
    public static void autoCloseOff(){
     BriefDb.close=false;
    }
    
     public static void autoCloseOn(){
     BriefDb.close=true;
    }


    public static ArrayList query(Connection conn, String query, Object... filter) throws Exception {
        PreparedStatement stm = conn.prepareStatement(query);
        int i = 1;
        if (filter != null) {
            for (Object value : filter) {
                stm.setObject(i++, value);
            }
        }
        ResultSet rst = stm.executeQuery();
        ResultSetMetaData meta = rst.getMetaData();
        ArrayList<HashMap<String, Object>> tabela = new ArrayList<>();
        while (rst.next()) {
            HashMap<String, Object> linha = new HashMap<>();
            for (int x = 1; x <= meta.getColumnCount(); x++) {
                linha.put(meta.getColumnName(x).toUpperCase(), rst.getObject(meta.getColumnName(x)));
            }
            tabela.add(linha);
        }
        rst.close();
        stm.close();
        if(BriefDb.close){
        conn.close();
        }
        return tabela;
    }

     
    
    
    public static HashMap<Object, Object> crud(Connection conn, int action, String table, String pkName, Object pkValue, HashMap<Object, Object> values) throws Exception {
        HashMap<Object, Object> retuns = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        switch (action) {
            case 1:
                sql.append("insert into ");
                sql.append(table.trim());
                sql.append(values.keySet().toString().replaceAll("\\[", "(").replaceAll("\\]", ")"));
                sql.append(" values (");
                for (int i = 1; i <= values.size(); i++) {
                    sql.append("?");
                    if (i < values.size()) {
                        sql.append(",");
                    }
                }
                sql.append(");");
                break;
            case 2:
                sql.append("update ");
                sql.append(table.trim());
                sql.append(" set ");
                sql.append(values.keySet().toString().replaceAll("\\[", "").replaceAll(",", "=?,").replaceAll("\\]", "=?"));
                sql.append(" where ");
                sql.append(pkName.trim());
                sql.append("=?");
                break;
            case 3:
                sql.append("delete from ");
                sql.append(table.trim());
                sql.append(" where ");
                sql.append(pkName.trim());
                sql.append("=?");
                break;
            default:
                throw new Exception("Opção invalida, Selecione uma das constantes: [SimpleDb.INSERT, SimpleDb.UPDATE, SimpleDb.DELETE]");
        }
        PreparedStatement stm = null;
        if (action == BriefDb.INSERT) {
            stm = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
        } else {
            stm = conn.prepareStatement(sql.toString());
        }
        int i = 1;
        if (action == BriefDb.INSERT || action == BriefDb.UPDATE) {
            for (Object key : values.keySet()) {
                stm.setObject(i++, values.get(key));
            }
        }
        if (action == BriefDb.DELETE || action == BriefDb.UPDATE) {
            stm.setObject(action == BriefDb.DELETE ? 1 : i++, pkValue);
        }
        retuns.put("SQL", "{" + sql.toString() + "}");
        if (stm.executeUpdate() == 1) {
            retuns.put("STATUS", true);
            if (action == BriefDb.INSERT) {
                ResultSet rst = stm.getGeneratedKeys();
                if (rst.next()) {
                    retuns.put("PRIMARY_KEY", rst.getInt(1));
                }
                retuns.put("MSG", "Registro Nº.XX cadastrado com sucesso".replaceAll("XX", String.valueOf(rst.getInt(1))));
                rst.close();
            } else if (action == BriefDb.UPDATE || action == BriefDb.DELETE) {
                retuns.put("PRIMARY_KEY", pkValue);
                retuns.put("MSG", "Registro Nº.XX ACTION com sucesso".replaceAll("XX", pkValue.toString()).replaceAll("ACTION", (action == BriefDb.UPDATE ? "atualizado" : "removido")));
            }
        } else {
            retuns.put("STATUS", false);
            retuns.put("MSG", "Não foi possível realizar operação, provavelmente o registro não foi encontrado.");
            retuns.put("PRIMARY_KEY", pkValue);
        }
        stm.close();
         if(BriefDb.close){
        conn.close();
        }
        return retuns;
    }
    
       
     
}
