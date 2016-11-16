
package ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class PropertiesHelper {
    private Properties mProperties;

    private boolean isLoadSuccess = false;

    public PropertiesHelper(String propertiesFilePath) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesFilePath);
            if (inputStream != null) {
                mProperties = new Properties();
                mProperties.load(inputStream);
                isLoadSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(inputStream);
        }
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public ArrayList<String> getKeyList() {
        if (!isLoadSuccess) {
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();
        Set<Object> keyValue = mProperties.keySet();
        for (Iterator<Object> it = keyValue.iterator(); it.hasNext();) {
            String key = (String) it.next();
            list.add(key);
        }
        return list;
    }

    public String getValue(String key) {
        if (!isLoadSuccess) {
            return null;
        }
        return mProperties.getProperty(key);

    }

}
