DROP TABLE IF EXISTS analysis_result;
DROP TABLE IF EXISTS description;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS feed;
DROP TABLE IF EXISTS result;

CREATE TABLE description (
  id IDENTITY,
  code VARCHAR2(64) NOT NULL
);

CREATE TABLE item (
  id IDENTITY,
  title VARCHAR2(255) NOT NULL,
  link VARCHAR2(1024) NOT NULL,
  guid VARCHAR2(64) NOT NULL
);

CREATE TABLE result (
  id IDENTITY,
  code VARCHAR2(128)
);

CREATE TABLE feed (
  id IDENTITY,
  title VARCHAR2(255) NOT NULL,
  link VARCHAR2(1024) NOT NULL,
  guid VARCHAR2(64) NOT NULL
  last_build_date TIMESTAMP WITH TIME ZONE,
  feed_language VARCHAR2(16),
  result_id BIGINT(19) NOT NULL,
  FOREIGN KEY (result_id ) references result (id)
);

CREATE TABLE analysis_result (
  result_id BIGINT(19) NOT NULL,
  description_id BIGINT(19) NOT NULL,
  item_id BIGINT(19) NOT NULL,
  PRIMARY KEY (result_id, description_id, item_id),
  FOREIGN KEY (result_id) references result(id),
  FOREIGN KEY (description_id) references description(id),
  FOREIGN KEY (item_id) references item(id)
);
