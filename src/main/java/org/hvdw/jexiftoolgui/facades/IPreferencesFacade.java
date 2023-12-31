package org.hvdw.jexiftoolgui.facades;

public interface IPreferencesFacade {
    enum PreferenceKey {
        USE_LAST_OPENED_FOLDER("uselastopenedfolder"),
        LAST_OPENED_FOLDER("lastopenedfolder"),
        DEFAULT_START_FOLDER("defaultstartfolder"),
        ARTIST("artist"),  // xmp-dc:creator, exif:artist, iptc:by-line
        COPYRIGHTS("copyrights"),  // xmp-dc:rights, exif:Copyright, iptc:copyrightnotice
        CREDIT("credit"),  // xmp-photoshop:credit, iptc:credit
        VERSION_CHECK("versioncheck"),
        LAST_APPLICATION_VERSION("applicationVersion"),
        EXIFTOOL_PATH("exiftool"),
        METADATA_LANGUAGE("metadatalanguage"),
        RAW_VIEWER_PATH("rawviewer"),
        RAW_VIEWER_ALL_IMAGES("rawviewerallimages"),
        PREFERRED_APP_LANGUAGE("System default"),
        PREFERRED_FILEDIALOG("jfilechooser"),
        LOG_LEVEL("loglevel"),
        SHOW_DECIMAL_DEGREES("showdecimaldegrees"), // exiftool shows coordinates by default as Deg Min Sec
        USE_G1_GROUP ("useg1group"),
        USER_DEFINED_FILE_FILTER("userdefinedfilefilter"),
        PRESERVE_MODIFY_DATE("preservemodifydate"),
        DUAL_COLUMN("dualcolumn"),
        GUI_WIDTH("guiwidth"), //rootpanel width
        GUI_HEIGHT("guiheight"), //rootpanel height
        SPLITPANEL_POSITION("splitpanelpostion"), //percentual position left/right splitpane
        LATITUDE("latitude"), // latitude for mapviewer screen
        LONGITUDE("longitude"),
        SORT_CATEGORIES_TAGS("sortcategoriestags"),
        ENABLE_STRUCTS("enable_struct_output"),
        USER_DEFINED_FONT("userdefinedfont"),
        USER_DEFINED_FONTSIZE("userdefinedfontsize")
        ;

        public final String key;
        PreferenceKey(String key) {
            this.key = key;
        }
    }

    boolean keyIsSet(PreferenceKey key);

    String getByKey(PreferenceKey key, int i);
    String getByKey(PreferenceKey key, String defaultValue);
    boolean getByKey(PreferenceKey key, boolean defaultValue);
    void storeByKey(PreferenceKey key, String value);
    void storeByKey(PreferenceKey key, boolean value);

    IPreferencesFacade defaultInstance = PreferencesFacade.thisFacade;
}
