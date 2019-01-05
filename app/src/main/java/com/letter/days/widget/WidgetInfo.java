package com.letter.days.widget;

import org.litepal.crud.LitePalSupport;

public class WidgetInfo extends LitePalSupport {

    public static final int WIDGET_TYPE_NORMAL = 0;
    public static final int WIDGET_TYPE_CLOSEST = 1;

    private int id;
    private int widgetId;
    private int type;
    private int anniId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAnniId() {
        return anniId;
    }

    public void setAnniId(int anniId) {
        this.anniId = anniId;
    }
}
