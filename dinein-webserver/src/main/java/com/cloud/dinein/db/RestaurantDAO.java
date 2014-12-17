package com.cloud.dinein.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface RestaurantDAO {
	
	 @SqlUpdate("create table if not exists restaurant_table (name varchar(100) primary key, description varchar(100)")
	  void createRestaurantTable();

	  //TODO : Ideally return an int and do error checking for the client. 
	  @SqlUpdate("insert into restaurant_table (name, description) values (:name, :description)")
	  void insert(@Bind("name") String name, @Bind("description") String description);

	  @SqlQuery("select description from restaurant_table where name = :name")
	  String getDealByName(@Bind("name") String name);
	  
}
