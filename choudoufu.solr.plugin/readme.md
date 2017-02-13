<filter>
    <filter-name>SolrCustomFilter</filter-name>
    <filter-class>org.apache.solr.servlet.SolrCustomFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>SolrCustomFilter</filter-name>
    <url-pattern>/custom/*</url-pattern>
  </filter-mapping>
  