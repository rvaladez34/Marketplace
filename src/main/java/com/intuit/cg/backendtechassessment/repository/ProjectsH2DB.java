package com.intuit.cg.backendtechassessment.repository;

import com.intuit.cg.backendtechassessment.model.Bid;
import com.intuit.cg.backendtechassessment.model.Price;
import com.intuit.cg.backendtechassessment.model.Project;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedList;

@Component
public class ProjectsH2DB implements MyRepository {

    Log LOG = LogFactory.getLog(MyRepository.class);

    private Connection conn;

    public ProjectsH2DB(
            @Value("${spring.datasource.url}")
                    String db_url,
            @Value("${spring.datasource.username}")
                    String db_username,
            @Value("${spring.datasource.password}")
                    String db_password) throws Exception {

        //establish db connection
        this.conn = DriverManager.getConnection(db_url, db_username, db_password);

        //create tables
        Statement createTablesStatement = conn.createStatement();

        createTablesStatement.execute(SqlQueries.CREATE_MARKETPLACE_SCHEMA);
        createTablesStatement.execute(SqlQueries.USE_MARKETPLACE);
        createTablesStatement.execute(SqlQueries.CREATE_PROJECT_TABLE);
        createTablesStatement.execute(SqlQueries.CREATE_BID_TABLE);

        createTablesStatement.close();

    }


    @Override
    public Project getProject(Integer id) {
        Project p = null;
        try {
            ResultSet rs = conn.createStatement().executeQuery(SqlQueries.GET_PROJECT);
            while (rs.next()) {
                p = new Project()
                        .setId(rs.getInt(SqlQueries.PROJECT_ID))
                        .setSeller(new com.intuit.cg.backendtechassessment.model.Person()
                                .setName(rs.getString(SqlQueries.SELLER_NAME))
                                .setEmail(rs.getString(SqlQueries.SELLER_EMAIL)))
                        .setMaxBudget(new Price()
                                .setValue(new BigDecimal(rs.getFloat(SqlQueries.MAX_BUDGET))))
                        .setClosingDate(rs.getString(SqlQueries.CLOSING_DATE))
                        .setDescription(rs.getString(SqlQueries.DESCRIPTION));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (null != p) {
            //get bids ordered in ASC by bid amount
            LinkedList<Bid> bids = getBids(p.getId());
            if (!bids.isEmpty()) p.setLowestBid(bids.get(0));
            p.setAllBids(bids);
        }
        return p;
    }

    /**
     * Get all bids for given projectId
     *
     * @param projectId
     * @return
     */
    private LinkedList<Bid> getBids(Integer projectId) {
        LinkedList<Bid> bids = new LinkedList<>();
        try {
            ResultSet rs = conn.createStatement().executeQuery(SqlQueries.GET_BIDS);
            while (rs.next()) {
                Bid b = new Bid()
                        .setBuyer(new com.intuit.cg.backendtechassessment.model.Person()
                                .setName(rs.getString(SqlQueries.BUYER_NAME))
                                .setEmail(rs.getString(SqlQueries.BUYER_EMAIL)))
                        .setAmount(new Price()
                                .setValue(new BigDecimal(rs.getFloat(SqlQueries.BID_AMOUNT))));
                Float autoBid = rs.getFloat(SqlQueries.AUTO_BID);
                if (!rs.wasNull()) b.setAutoBid(new Price().setValue(new BigDecimal(autoBid)));
                bids.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bids;
    }

    @Override
    public Project addProject(Project project) {
        Project p = null;
        Integer projectId = null;
        try {
            String insert = SqlQueries.INSERT_PROJECT +
                    SqlQueries.PROJECT_VALUES
                            .replace(SqlQueries.SELLER_NAME, project.getSeller().getName())
                            .replace(SqlQueries.SELLER_EMAIL, project.getSeller().getEmail())
                            .replace(SqlQueries.MAX_BUDGET, project.getMaxBudget().getValue().toString())
                            .replace(SqlQueries.CLOSING_DATE, project.getClosingDate())
                            .replace(SqlQueries.DESCRIPTION, project.getDescription());

            Statement statement = conn.createStatement();
            statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);

            //get generated project id
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                projectId = generatedKeys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (null != projectId) {
            p = project.setId(projectId);
        }

        return p;
    }

    @Override
    public Project addBid(Integer id, Bid bid) {
        Project p = null;
        Integer bidId = null;
        try {
            String values = SqlQueries.BID_VALUES
                    .replace(SqlQueries.BUYER_NAME, bid.getBuyer().getName())
                    .replace(SqlQueries.BUYER_EMAIL, bid.getBuyer().getEmail())
                    .replace(SqlQueries.BID_AMOUNT, bid.getAmount().getValue().toString())
                    .replace(SqlQueries.PROJECT_ID, id.toString());

            if (null != bid.getAutoBid()) {
                values = values.replace(SqlQueries.AUTO_BID, "'"+bid.getAutoBid().getValue().toString()+"'");
            } else {
                values = values.replace(SqlQueries.AUTO_BID, "NULL");
            }
            String insert = SqlQueries.INSERT_BID+values;

            Statement statement = conn.createStatement();
            statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);

            //get generated project id
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                bidId = generatedKeys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (null != bidId) {
            p = getProject(id);
        }

        return p;

    }

    @Override
    public Project updateCurrentBid(Integer id, BigDecimal newAmount) {
        //get lowest bid
        Bid bid = getBids(id).get(0);

        //update amount
        bid.getAmount().setValue(newAmount);

        //add new bid
        return addBid(id, bid);

    }
}
