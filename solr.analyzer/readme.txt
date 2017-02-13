项目描述：solr 分词器项目
   
   1, IKAnalyzer     ---IK分词器      （适用：模糊搜索    如：标题,简介描述等）
   
   2, AddressAnalyzer ---地址分词器 （参数：province/city）   （适用：根据用户地址  按省或市 检索 或统计）
   
   3, MinSplitAnalyzer  ---最小细粒度分词器    （适用：非词组属性数据，如：用户姓名搜索）
   
   4, PinyinAnalyzer    ---拼音分词器
   
   5, PositiveContinuousSplitAnalyzer   ---正向连续切割分词器   （适用：搜索框下拉自动补全）
   
   6, SeparatorAnalyzer    ---分隔符分词器（适用： 树结构 与 数组 类型数据分词）