<h2>Classe de manipulação de DQL e DML</h2>

<p> Esta biblioteca tem como finalidade simplificar o processo de escrita de algumas das funções básicas de consulta e alteração de dados no banco de dados. </p>
<p><a href="https://github.com/wellingtonsluzhotmailcom/BriefDb/raw/master/BriefDb.jar" download="download"> Clique aqui para fazer o download</a>. </p>
<hr />

<pre>
     public static void main(String[] args) {
        Connection conn = null; 
        try {

            // conexão com o banco de dados
            conn = ConexaoDb.getConnection(); 

            //valores para o banco de dados
            HashMap<Object, Object> valeus = new HashMap<>();
            // coluna , valor
            valeus.put("descricao", "Gerente");

           // retorna um HashMap com o resultado da operação do insert
           // BriefDb.crud(conexao_db, tipo_de_acao, nome_tabela, nome_chave_primaria, valor_chave_primaria, 
           // valores_de_entrada)
           System.out.println(BriefDb.crud(conn, BriefDb.INSERT, "CARGO", null, null, valeus));
   
           /*  Resposta caso execute o comando:  {MSG=Registro Nº.27 cadastrado com sucesso, STATUS=true, PRIMARY_KEY=27, 
           SQL= {insert into CARGO(descricao) values (?);}} */

           // conexão com o banco de dados;           
           conn = ConexaoDb.getConnection(); 
         
            // retorna um HashMap com o resultado da operação de delete
            System.out.println(BriefDb.crud(conn , BriefDb.DELETE, "CARGO", "ID_CARGO", 26, null));
        
           /*  Resposta caso execute o comando: {MSG=Registro Nº.26 removido com sucesso, STATUS=true, PRIMARY_KEY=26, 
            SQL= {delete from CARGO where ID_CARGO=?}} */


           valeus = new HashMap<>();
           // coluna , valor
           valeus.put("ID_CARGO", 3);
           valeus.put("descricao", "caixa");

            // conexão com o banco de dados;
            conn = ConexaoDb.getConnection(); 
            
            // retorna um HashMap com o resultado da operação de update
            System.out.println(BriefDb.crud(conn, BriefDb.UPDATE, "CARGO", "ID_CARGO", 27, valeus ));

           /*  Resposta caso execute o comando: {MSG=Registro Nº.27 atualizado com sucesso, STATUS=true, PRIMARY_KEY=27, 
           SQL={update CARGO set ID_CARGO=?, descricao=? where ID_CARGO=?}} */
         
            // conexão com o banco de dados;
           conn = ConexaoDb.getConnection(); 
           // retorna um HashMap com os dados da consulta do banco de dados
           //System.out.println(BriefDb.query(conexao_db, consulta_sql, paramentros_de_consulta ));
        
            System.out.println(BriefDb.query(conn, "Select * from cargo ", null));

            /* Resposta caso execute o comando: [{ID_CARGO=11, DESCRICAO=Frente de caixa}, {ID_CARGO=14, 
            DESCRICAO=Vendedor}, {ID_CARGO=17, DESCRICAO=Administrador}, {ID_CARGO=2, DESCRICAO=Repositor}, {ID_CARGO=1, 
            DESCRICAO=Gerente},  {ID_CARGO=28, DESCRICAO=caixa}, {ID_CARGO=3, DESCRICAO=caixa}] */

             // conexão com o banco de dados; 
             conn = ConexaoDb.getConnection();

            // obs. a ordem dos parametros deve ser passada conforme a referencia na consulta sql
            System.out.println(conn, "Select * from cargo where id_cargo=? or descricao like ? ", 1, "%ixa"));

            /* Resposta caso execute o comando: [{ID_CARGO=11, DESCRICAO=Frente de caixa}, {ID_CARGO=1, 
            DESCRICAO=Gerente}, {ID_CARGO=28, DESCRICAO=caixa}, {ID_CARGO=3, DESCRICAO=caixa}] */

           } catch (Exception ex) {
              System.err.println(ex.getMessage());
         }
    }
</pre>
