package org.cProc.index.BPlusTree.query;



import org.cProc.index.BPlusTree.BTreeType;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.index.BPlusTree.query.impl.Query;
import org.cProc.index.BPlusTree.query.impl.QueryMergerBPlusTree;



public class QueryInstanceFactory<T extends Data> {
	
	
		public  QueryInterface<T> getQueryInstance(String indexName)
		{
			Query<T> query = new Query<T>(indexName);
			if(BTreeType.MegerBTreeType == query.getType())
			{
				query.queryClose();
				return new QueryMergerBPlusTree<T>(indexName);
			}
			return query;
//			if(indexName.matches(BTreeType.matchOrgName))
//			{
//				return new Query<T>(indexName);
//			}
//			else if(indexName.matches(BTreeType.matchMergeName))
//			{
//				return new QueryMergerBPlusTree<T>(indexName);
//			}
		//	return null;
		}
}
