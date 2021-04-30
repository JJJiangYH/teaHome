package com.tea.sql.Dao;

import com.tea.sql.Bean.Knowledge;
import com.tea.sql.Utils.KnowledgeDBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeDao {
    /**
     * 添加一条Knowledge数据。
     * 如果存在authorName则直接添加，否则先添加author信息，在进行添加
     *
     * @param knowledge 结构信息
     */
    public static void addKnowledge(Knowledge knowledge) {
        Connection connection = KnowledgeDBUtils.getConnection();
        if (knowledge == null) {
            return;
        }

        //检查是否存在authorName,如果存在就获取id，否则新建一条数据
        String selectSql = "SELECT authorId from author where authorName = ?";
        String addAuthorSql = "INSERT INTO author (authorName) VALUES ('" + knowledge.getAuthorName() + "');";
        int authorId = 0;

        try {
            connection.setAutoCommit(false);

            //预编译
            PreparedStatement ptmt = connection.prepareStatement(selectSql);

            ptmt.setString(1, knowledge.getAuthorName());

            ResultSet rs = ptmt.executeQuery();

            if (!rs.next()) {
                Statement statement = connection.createStatement();

                statement.executeUpdate(addAuthorSql, Statement.RETURN_GENERATED_KEYS);

                rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    authorId = rs.getInt(1);
                }
            } else {
                authorId = rs.getInt("authorId");
            }
            if (authorId != 0) {
                String addKnowledgeSql = "INSERT INTO knowledge (createTime, authorId, title,havePic) VALUES (CURRENT_DATE,?,?,?)";
                PreparedStatement addKnowledgePtmt = connection.prepareStatement(addKnowledgeSql);

                addKnowledgePtmt.setInt(1, authorId);
                addKnowledgePtmt.setString(2, knowledge.getTitle());
                addKnowledgePtmt.setBoolean(3, knowledge.getHavePic());

                addKnowledgePtmt.execute();
            } else {
                throw new SQLDataException();
            }
        } catch (SQLIntegrityConstraintViolationException sqlIntegrityException) {
            sqlIntegrityException.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            //title重复
        } catch (SQLException sqlException) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            sqlException.printStackTrace();
        } finally {
            try {
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    public static List<Knowledge> query() {
        List<Knowledge> gs = new ArrayList<>();
        Runnable runnable = () -> {
            Connection connection = KnowledgeDBUtils.getConnection();
            ResultSet rs = null;

            try {
                Statement stmt = connection.createStatement();
                rs = stmt.executeQuery(
                        "select createTime, authorName, title, havePic, click " +
                                "from knowledge,author " +
                                "where knowledge.authorId = author.authorId;");

                Knowledge k;
                while (rs.next()) {
                    k = new Knowledge();

                    k.setAuthorName(rs.getString("authorName"));
                    k.setCreateTime(rs.getDate("createTime"));
                    k.setTitle(rs.getString("title"));
                    k.setHavePic(rs.getBoolean("havePic"));
                    k.setClick(rs.getInt("click"));

                    gs.add(k);
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        };
        runnable.run();
        return gs;
    }

    public static void delKnowledge(String title) {
        Connection connection = KnowledgeDBUtils.getConnection();
        String delSql = "DELETE FROM knowledge WHERE title = ?;";

        try {
            //删除title
            PreparedStatement delPtmt = connection.prepareStatement(delSql);

            delPtmt.setString(1, title);

            delPtmt.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    /**
     * 修改title所对应的数据库信息
     *
     * @param title 修改前的Title
     * @param k     要改成的信息
     * @throws SQLException
     */
    public static void updateKnowledge(String title, Knowledge k) throws SQLException {
        Connection connection = KnowledgeDBUtils.getConnection();

        String updateSql = "UPDATE knowledge k,author a " +
                "SET k.title = ?, a.authorName = ?, k.havePic = ? " +
                "where k.title = ? AND k.authorId = a.authorId;";

        connection.setAutoCommit(false);
        try {
            PreparedStatement updatePtmt = connection.prepareStatement(updateSql);

            updatePtmt.setString(1, k.getTitle());
            updatePtmt.setString(2, k.getAuthorName());
            updatePtmt.setBoolean(3, k.getHavePic());
            updatePtmt.setString(4, title);

            updatePtmt.execute();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.commit();
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        connection.setAutoCommit(true);
    }

    public static Knowledge getKnowledgeByTitle(String title) {
        Connection connection = KnowledgeDBUtils.getConnection();
        String selectSql =
                "select createTime, authorName, title, havePic " +
                        "from knowledge,author " +
                        "where knowledge.authorId = author.authorId AND title = ?;";

        PreparedStatement selectPtmt;
        Knowledge knowledge = null;

        try {
            selectPtmt = connection.prepareStatement(selectSql);

            selectPtmt.setString(1, title);

            selectPtmt.execute();

            ResultSet rs = selectPtmt.executeQuery();

            if (rs.next()) {
                knowledge = new Knowledge();
                knowledge.setAuthorName(rs.getString("authorName"));
                knowledge.setTitle(rs.getString("title"));
                knowledge.setCreateTime(rs.getDate("createTime"));
                knowledge.setHavePic(rs.getBoolean("havePic"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return knowledge;
    }
}
