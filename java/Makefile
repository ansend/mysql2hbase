JAVAC=../jdk1.7.0_79/bin/javac

all: HColumnEnum.class HbaseClientCopy.class Mysql2HbaseCopy.class HColumnEnumFile.class HbaseClientFile.class  Mysql2HbaseFile.class

HColumnEnum.class: HColumnEnum.java
	$(JAVAC) -cp .:./lib/mysql-connector-java-5.1.39/*:./lib/hbase-0.98.17_lib/*  HColumnEnum.java

HbaseClientCopy.class: HbaseClientCopy.java
	$(JAVAC) -cp .:./lib/mysql-connector-java-5.1.39/*:./lib/hbase-0.98.17_lib/*  HbaseClientCopy.java

Mysql2HbaseCopy.class: Mysql2HbaseCopy.java
	$(JAVAC) -cp .:./lib/mysql-connector-java-5.1.39/*:./lib/hbase-0.98.17_lib/*  Mysql2HbaseCopy.java

HColumnEnumFile.class: HColumnEnumFile.java
	$(JAVAC) -cp .:./lib/mysql-connector-java-5.1.39/*:./lib/hbase-0.98.17_lib/*  HColumnEnumFile.java

HbaseClientFile.class: HbaseClientFile.java
	$(JAVAC) -cp .:./lib/mysql-connector-java-5.1.39/*:./lib/hbase-0.98.17_lib/*  HbaseClientFile.java

Mysql2HbaseFile.class: Mysql2HbaseFile.java
	$(JAVAC) -cp .:./lib/mysql-connector-java-5.1.39/*:./lib/hbase-0.98.17_lib/*  Mysql2HbaseFile.java

clean:
	rm *.class

.PHONY: clean

