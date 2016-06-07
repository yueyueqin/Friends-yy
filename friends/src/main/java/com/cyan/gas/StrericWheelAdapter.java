
package com.cyan.gas;


/**
 * Numeric Wheel adapter.
 */
public class StrericWheelAdapter implements WheelAdapter
{

    /**
     * The default min value
     */
    private String[] strContents;

    /**
     * @param strContents
     */
    public StrericWheelAdapter(String[] strContents)
    {
        this.strContents = strContents;
    }


    public String[] getStrContents()
    {
        return strContents;
    }


    public void setStrContents(String[] strContents)
    {
        this.strContents = strContents;
    }


    public String getItem(int index)
    {
        if (index >= 0 && index < getItemsCount()) {
            return strContents[index];
        }
        return null;
    }

    public int getItemsCount()
    {
        return strContents.length;
    }

    public int getMaximumLength()
    {
        int maxLen = 5;
        return maxLen;
    }
}
