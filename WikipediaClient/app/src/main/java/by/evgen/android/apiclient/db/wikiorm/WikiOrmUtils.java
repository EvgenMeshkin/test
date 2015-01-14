package by.evgen.android.apiclient.db.wikiorm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 15.12.2014.
 */
public class WikiOrmUtils {
    private final static HashMap<Class<? extends WikiOrmEntity>, List<WikiOrmEntity>> cashedEntityLists = new HashMap<Class<? extends WikiOrmEntity>, List<WikiOrmEntity>>();

    private static List<WikiOrmEntity> getCashedList(Class<? extends WikiOrmEntity> entityClass) {
        if (!cashedEntityLists.containsKey(entityClass))
            cashedEntityLists.put(entityClass, new ArrayList<WikiOrmEntity>());
        return cashedEntityLists.get(entityClass);
    }

}
