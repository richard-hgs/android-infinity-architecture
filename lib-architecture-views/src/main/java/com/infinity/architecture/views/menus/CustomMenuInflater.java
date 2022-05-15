package com.infinity.architecture.views.menus;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//import com.android.internal.view.menu.MenuItemImpl;
public class CustomMenuInflater {

}

/**
 * This class is used to instantiate menu XML files into Menu objects.
 * <p>
 * For performance reasons, menu inflation relies heavily on pre-processing of
 * XML files that is done at build time. Therefore, it is not currently possible
 * to use MenuInflater with an XmlPullParser over a plain XML file at runtime;
 * it only works with an XmlPullParser returned from a compiled resource (R.
 * <em>something</em> file.)
 */
//public class CustomMenuInflater {
//    private static final String LOG_TAG = "MenuInflater";
//
//    /** Menu tag name in XML. */
//    private static final String XML_MENU = "menu";
//
//    /** Group tag name in XML. */
//    private static final String XML_GROUP = "group";
//
//    /** Item tag name in XML. */
//    private static final String XML_ITEM = "item";
//
//    private static final int NO_ID = 0;
//
//    private static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[] {Context.class};
//
//    private static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
//
//    private final Object[] mActionViewConstructorArguments;
//
//    private final Object[] mActionProviderConstructorArguments;
//
//    private Context mContext;
//    private Object mRealOwner;
//
//    /**
//     * Constructs a menu inflater.
//     *
//     * @see Activity#getMenuInflater()
//     */
//    public CustomMenuInflater(Context context) {
//        mContext = context;
//        mRealOwner = context;
//        mActionViewConstructorArguments = new Object[] {context};
//        mActionProviderConstructorArguments = mActionViewConstructorArguments;
//    }
//
//    /**
//     * Constructs a menu inflater.
//     *
//     * @see Activity#getMenuInflater()
//     * @hide
//     */
//    public CustomMenuInflater(Context context, Object realOwner) {
//        mContext = context;
//        mRealOwner = realOwner;
//        mActionViewConstructorArguments = new Object[] {context};
//        mActionProviderConstructorArguments = mActionViewConstructorArguments;
//    }
//
//    /**
//     * Inflate a menu hierarchy from the specified XML resource. Throws
//     * {@link InflateException} if there is an error.
//     *
//     * @param menuRes Resource ID for an XML layout resource to load (e.g.,
//     *            <code>R.menu.main_activity</code>)
//     * @param menu The Menu to inflate into. The items and submenus will be
//     *            added to this Menu.
//     */
//    public void inflate(int menuRes, ArrayList<Integer> intArray) { // TODO inflate to where?
//        XmlResourceParser parser = null;
//        try {
//            parser = mContext.getResources().getLayout(menuRes);
//            AttributeSet attrs = Xml.asAttributeSet(parser);
//            Log.d("attrs inflate", Integer.toString(attrs.getAttributeCount()));
//            parseMenu(parser, attrs, intArray);
//        } catch (XmlPullParserException e) {
//            throw new InflateException("Error inflating menu XML", e);
//        } catch (IOException e) {
//            throw new InflateException("Error inflating menu XML", e);
//        } finally {
//            if (parser != null) parser.close();
//        }
//    }
//
//    /**
//     * Called internally to fill the given menu. If a sub menu is seen, it will
//     * call this recursively.
//     */
//    private void parseMenu(XmlPullParser parser, AttributeSet attrs, ArrayList<Integer> intArray) // TODO parse tp where
//            throws XmlPullParserException, IOException {
//        MenuState menuState = new MenuState();
//        //intArray = new ArrayList<Integer>();
//        Log.d("attrs parse", Integer.toString(attrs.getAttributeCount()));
//
//        int eventType = parser.getEventType();
//        String tagName;
//        boolean lookingForEndOfUnknownTag = false;
//        String unknownTagName = null;
//
//
//
//        // This loop will skip to the menu start tag
//        do {
//            if (eventType == XmlPullParser.START_TAG) {
//                tagName = parser.getName();
//                if (tagName.equals(XML_MENU)) {
//                    // Go to next tag
//                    eventType = parser.next();
//                    break;
//                }
//
//                throw new RuntimeException("Expecting menu, got " + tagName);
//            }
//            eventType = parser.next();
//        } while (eventType != XmlPullParser.END_DOCUMENT);
//
//        boolean reachedEndOfMenu = false;
//        while (!reachedEndOfMenu) {
//            switch (eventType) {
//                case XmlPullParser.START_TAG:
//                    if (lookingForEndOfUnknownTag) {
//                        break;
//                    }
//
//                    tagName = parser.getName();
//                    if (tagName.equals(XML_GROUP)) {
//                        /* menuState.readGroup(attrs);*/
//                    } else if (tagName.equals(XML_ITEM)) {
//                        menuState.readItem(attrs);
//                    } else if (tagName.equals(XML_MENU)) {
//                        // A menu start tag denotes a submenu for an item
//                        //SubMenu subMenu = menuState.addSubMenuItem();
//                        // TODO what if there is a submenu
//
//
//                        // Parse the submenu into returned SubMenu
//                        //parseMenu(parser, attrs, subMenu);
//                    } else {
//                        lookingForEndOfUnknownTag = true;
//                        unknownTagName = tagName;
//                    }
//                    break;
//
//                case XmlPullParser.END_TAG:
//                    tagName = parser.getName();
//                    if (lookingForEndOfUnknownTag && tagName.equals(unknownTagName)) {
//                        lookingForEndOfUnknownTag = false;
//                        unknownTagName = null;
//                    } else if (tagName.equals(XML_GROUP)) {
//                        menuState.resetGroup();
//                    } else if (tagName.equals(XML_ITEM)) {
//                        // Add the item if it hasn't been added (if the item was
//                        // a submenu, it would have been added already)
//
//                        if (!menuState.hasAddedItem()) {
//                            /*if (menuState.itemActionProvider != null &&
//                                    menuState.itemActionProvider.hasSubMenu()) {
//                                menuState.addSubMenuItem();
//                            } else {
//                                menuState.addItem();
//                            }*/
//                            menuState.addItem(intArray);
//                        }
//                        // TODO addItem() goes here
//                    } else if (tagName.equals(XML_MENU)) {
//                        reachedEndOfMenu = true;
//                    }
//                    break;
//
//                case XmlPullParser.END_DOCUMENT:
//                    throw new RuntimeException("Unexpected end of document");
//            }
//
//            eventType = parser.next();
//        }
//    }
//
//    /*private static class InflatedOnMenuItemClickListener // TODO onClickListener?
//            implements MenuItem.OnMenuItemClickListener {
//        private static final Class<?>[] PARAM_TYPES = new Class[] { MenuItem.class };
//
//        private Object mRealOwner;
//        private Method mMethod;
//
//        public InflatedOnMenuItemClickListener(Object realOwner, String methodName) {
//            mRealOwner = realOwner;
//            Class<?> c = realOwner.getClass();
//            try {
//                mMethod = c.getMethod(methodName, PARAM_TYPES);
//            } catch (Exception e) {
//                InflateException ex = new InflateException(
//                        "Couldn't resolve menu item onClick handler " + methodName +
//                        " in class " + c.getName());
//                ex.initCause(e);
//                throw ex;
//            }
//        }
//
//        public boolean onMenuItemClick(MenuItem item) {
//            try {
//                if (mMethod.getReturnType() == Boolean.TYPE) {
//                    return (Boolean) mMethod.invoke(mRealOwner, item);
//                } else {
//                    mMethod.invoke(mRealOwner, item);
//                    return true;
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }*/
//
//    /**
//     * State for the current menu.
//     * <p>
//     * Groups can not be nested unless there is another menu (which will have
//     * its state class).
//     */
//    private class MenuState { // TODO MenuState needs menu?
//        //private Menu menu;
//
//        /*
//         * Group state is set on items as they are added, allowing an item to
//         * override its group state. (As opposed to set on items at the group end tag.)
//         */
//        private int groupId;
//        private int groupCategory;
//        private int groupOrder;
//        private int groupCheckable;
//        private boolean groupVisible;
//        private boolean groupEnabled;
//
//        private boolean itemAdded;
//        private int itemId;
//        private int itemCategoryOrder;
//        private CharSequence itemTitle;
//        private CharSequence itemTitleCondensed;
//        private int itemIconResId;
//        private char itemAlphabeticShortcut;
//        private char itemNumericShortcut;
//        /**
//         * Sync to attrs.xml enum:
//         * - 0: none
//         * - 1: all
//         * - 2: exclusive
//         */
//        private int itemCheckable;
//        private boolean itemChecked;
//        private boolean itemVisible;
//        private boolean itemEnabled;
//
//        /**
//         * Sync to attrs.xml enum, values in MenuItem:
//         * - 0: never
//         * - 1: ifRoom
//         * - 2: always
//         * - -1: Safe sentinel for "no value".
//         */
//        private int itemShowAsAction;
//
//        private int itemActionViewLayout;
//        private String itemActionViewClassName;
//        private String itemActionProviderClassName;
//
//        private String itemListenerMethodName;
//
//        private android.view.ActionProvider itemActionProvider;
//
//        private static final int defaultGroupId = NO_ID;
//        private static final int defaultItemId = NO_ID;
//        private static final int defaultItemCategory = 0;
//        private static final int defaultItemOrder = 0;
//        private static final int defaultItemCheckable = 0;
//        private static final boolean defaultItemChecked = false;
//        private static final boolean defaultItemVisible = true;
//        private static final boolean defaultItemEnabled = true;
//
//        static final int CATEGORY_MASK = 0xffff0000; // TODO may change with new versions of Android - from android.view.Menu
//        static final int USER_MASK = 0x0000ffff; // TODO may change with new versions of Android - from android.view.Menu
//
//        public MenuState() {
//            //this.menu = menu;
//
//            resetGroup();
//        }
//
//        public void resetGroup() {
//            groupId = defaultGroupId;
//            groupCategory = defaultItemCategory;
//            groupOrder = defaultItemOrder;
//            groupCheckable = defaultItemCheckable;
//            groupVisible = defaultItemVisible;
//            groupEnabled = defaultItemEnabled;
//        }
//
//        /**
//         * Called when the parser is pointing to a group tag.
//         */
//        /*public void readGroup(AttributeSet attrs) {
//            Resources res = Resources.getSystem();
//            TypedArray a = mContext.obtainStyledAttributes(attrs,
//                    toIntArray(res.getIdentifier(R.styleable.MenuGroup)));
//
//            groupId = a.getResourceId(res.getIdentifier("MenuGroup_id", "styleable", "R"), defaultGroupId);
//            groupCategory = a.getInt(res.getIdentifier("MenuGroup_menuCategory", "styleable", "R"), defaultItemCategory);
//            groupOrder = a.getInt(res.getIdentifier("MenuGroup_orderInCategory", "styleable", "R"), defaultItemOrder);
//            groupCheckable = a.getInt(res.getIdentifier("MenuGroup_checkableBehavior", "styleable", "R"), defaultItemCheckable);
//            groupVisible = a.getBoolean(res.getIdentifier("MenuGroup_visible", "styleable", "R"), defaultItemVisible);
//            groupEnabled = a.getBoolean(res.getIdentifier("MenuGroup_enabled", "styleable", "R"), defaultItemEnabled);
//
//            a.recycle();
//        }*/
//
//        /**
//         * Called when the parser is pointing to an item tag.
//         */
//        public void readItem(AttributeSet attrs) {
//            Resources res = Resources.getSystem();
//            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItem);
//            Log.d("a how many items", Integer.toString(a.length()));
//            Log.d("a 1", "nout:" + attrs.getAttributeValue("http://schemas.android.com/apk/res/res-auto", "MenuItem_id"));
//
//
//            // Inherit attributes from the group as default value
//            itemId = a.getResourceId(R.styleable.MenuItem_id, defaultItemId);
//            /*final int category = a.getInt(res.getIdentifier("MenuItem_menuCategory", "styleable", "R"), groupCategory);
//            final int order = a.getInt(res.getIdentifier("MenuItem_orderInCategory", "styleable", "R"), groupOrder);
//            itemCategoryOrder = (category & CATEGORY_MASK) | (order & USER_MASK);*/
//            itemTitle = a.getString(R.styleable.MenuItem_title);
//            Log.d("itemTitle", a.getString(R.styleable.MenuItem_title));
//            /*itemTitleCondensed = a.getText(res.getIdentifier("MenuItem_titleCondensed", "styleable", "R"));*/
//            itemIconResId = a.getResourceId(R.styleable.MenuItem_icon, 0);
//            Log.d("placed:itemIconResId", Integer.toString(itemIconResId));
//            /*itemAlphabeticShortcut =
//                    getShortcut(a.getString(res.getIdentifier("MenuItem_alphabeticShortcut", "styleable", "R")));
//            itemNumericShortcut =
//                    getShortcut(a.getString(res.getIdentifier("MenuItem_numericShortcut", "styleable", "R")));
//            if (a.hasValue(res.getIdentifier("MenuItem_checkable", "styleable", "R"))) {
//                // Item has attribute checkable, use it
//                itemCheckable = a.getBoolean(res.getIdentifier("MenuItem_checkable", "styleable", "R"), false) ? 1 : 0;
//            } else {
//                // Item does not have attribute, use the group's (group can have one more state
//                // for checkable that represents the exclusive checkable)
//                itemCheckable = groupCheckable;
//            }
//            itemChecked = a.getBoolean(res.getIdentifier("MenuItem_checked", "styleable", "R"), defaultItemChecked);
//            itemVisible = a.getBoolean(res.getIdentifier("MenuItem_visible", "styleable", "R"), groupVisible);
//            itemEnabled = a.getBoolean(res.getIdentifier("MenuItem_enabled", "styleable", "R"), groupEnabled);
//            itemShowAsAction = a.getInt(res.getIdentifier("MenuItem_showAsAction", "styleable", "R"), -1);
//            itemListenerMethodName = a.getString(res.getIdentifier("MenuItem_onClick", "styleable", "R"));
//            itemActionViewLayout = a.getResourceId(res.getIdentifier("MenuItem_actionLayout", "styleable", "R"), 0);
//            itemActionViewClassName = a.getString(res.getIdentifier("MenuItem_actionViewClass", "styleable", "R"));
//            itemActionProviderClassName = a.getString(res.getIdentifier("MenuItem_actionProviderClass", "styleable", "R"));
//
//            Log.d("a", Integer.toString(a.getIndexCount()));
//
//
//            final boolean hasActionProvider = itemActionProviderClassName != null;
//            if (hasActionProvider && itemActionViewLayout == 0 && itemActionViewClassName == null) {
//                itemActionProvider = newInstance(itemActionProviderClassName,
//                            ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE,
//                            mActionProviderConstructorArguments);
//            } else {
//                if (hasActionProvider) {
//                    Log.w(LOG_TAG, "Ignoring attribute 'actionProviderClass'."
//                            + " Action view already specified.");
//                }
//                itemActionProvider = null;
//            }*/
//
//            a.recycle();
//
//            itemAdded = false;
//        }
//
//        private char getShortcut(String shortcutString) {
//            if (shortcutString == null) {
//                return 0;
//            } else {
//                return shortcutString.charAt(0);
//            }
//        }
//
////        private void setItem(MenuItem item) {
////            item.setChecked(itemChecked)
////                .setVisible(itemVisible)
////                .setEnabled(itemEnabled)
////                .setCheckable(itemCheckable >= 1)
////                .setTitleCondensed(itemTitleCondensed)
////                .setIcon(itemIconResId)
////                .setAlphabeticShortcut(itemAlphabeticShortcut)
////                .setNumericShortcut(itemNumericShortcut);
////
////            if (itemShowAsAction >= 0) {
////                item.setShowAsAction(itemShowAsAction);
////            }
////
////            if (itemListenerMethodName != null) {
////                if (mContext.isRestricted()) {
////                    throw new IllegalStateException("The android:onClick attribute cannot "
////                            + "be used within a restricted context");
////                }
////                item.setOnMenuItemClickListener(
////                        new InflatedOnMenuItemClickListener(mRealOwner, itemListenerMethodName));
////            }
////
////            if (item instanceof MenuItemImpl) {
////                MenuItemImpl impl = (MenuItemImpl) item;
////                if (itemCheckable >= 2) {
////                    impl.setExclusiveCheckable(true);
////                }
////            }
////
////            boolean actionViewSpecified = false;
////            if (itemActionViewClassName != null) {
////                View actionView = (View) newInstance(itemActionViewClassName,
////                        ACTION_VIEW_CONSTRUCTOR_SIGNATURE, mActionViewConstructorArguments);
////                item.setActionView(actionView);
////                actionViewSpecified = true;
////            }
////            if (itemActionViewLayout > 0) {
////                if (!actionViewSpecified) {
////                    item.setActionView(itemActionViewLayout);
////                    actionViewSpecified = true;
////                } else {
////                    Log.w(LOG_TAG, "Ignoring attribute 'itemActionViewLayout'."
////                            + " Action view already specified.");
////                }
////            }
////            if (itemActionProvider != null) {
////                item.setActionProvider(itemActionProvider);
////            }
////        }
////
////        public void addItem() {
////            itemAdded = true;
////            setItem(menu.add(groupId, itemId, itemCategoryOrder, itemTitle));
////        }
////
////        public SubMenu addSubMenuItem() {
////            itemAdded = true;
////            SubMenu subMenu = menu.addSubMenu(groupId, itemId, itemCategoryOrder, itemTitle);
////            setItem(subMenu.getItem());
////            return subMenu;
////        }
//
//
//        public void addItem(ArrayList<Integer> integerArray) {
//            itemAdded = true;
//            integerArray.add(itemIconResId);
//            Log.d("itemIconResId", Integer.toString(itemIconResId));
//        }
//
//        public void addSubMenuItem() {
//            itemAdded = true;
//            // TODO add submenu item added method
//
//        }
//
//        public boolean hasAddedItem() {
//            return itemAdded;
//        }
//
//        @SuppressWarnings("unchecked")
//        private <T> T newInstance(String className, Class<?>[] constructorSignature,
//                                  Object[] arguments) {
//            try {
//                Class<?> clazz = mContext.getClassLoader().loadClass(className);
//                Constructor<?> constructor = clazz.getConstructor(constructorSignature);
//                return (T) constructor.newInstance(arguments);
//            } catch (Exception e) {
//                Log.w(LOG_TAG, "Cannot instantiate class: " + className, e);
//            }
//            return null;
//        }
//    }
//
//    public int[] toIntArray(int identifier) {
//        int[] newIntArray = new int[1];
//        newIntArray[0] = identifier;
//        return newIntArray;
//    }
//
//        <declare-styleable name="MenuItem">
//        <!-- The ID of the item. -->
//        <attr name="id" format="string" />
//        <!-- The title associated with the item. -->
//        <attr name="title" format="string" />
//        <!-- The condensed title associated with the item.  This is used in situations where the
//    normal title may be too long to be displayed. -->
//        <attr name="titleCondensed" format="string" />
//        <!-- The icon associated with this item.  This icon will not always be shown, so
//    the title should be sufficient in describing this item. -->
//        <attr name="icon" format="integer" />
//        <!-- The alphabetic shortcut key.  This is the shortcut when using a keyboard
//    with alphabetic keys. -->
//        <attr name="alphabeticShortcut" format="string" />
//        <!-- The numeric shortcut key.  This is the shortcut when using a numeric (e.g., 12-key)
//    keyboard. -->
//        <attr name="numericShortcut" format="string" />
//        <!-- Whether the item is capable of displaying a check mark. -->
//        <attr name="checkable" format="boolean" />
//        <!-- Name of a method on the Context used to inflate the menu that will be
//    called when the item is clicked. -->
//        <attr name="onClick" format="string" />
//        <!-- How this item should display in the Action Bar, if present. -->
//        <attr name="showAsAction">
//            <!-- Never show this item in an action bar, show it in the overflow menu instead.
//    Mutually exclusive with "ifRoom" and "always". -->
//            <flag name="never" value="0" />
//            <!-- Show this item in an action bar if there is room for it as determined
//    by the system. Favor this option over "always" where possible.
//    Mutually exclusive with "never" and "always". -->
//            <flag name="ifRoom" value="1" />
//            <!-- Always show this item in an actionbar, even if it would override
//    the system's limits of how much stuff to put there. This may make
//    your action bar look bad on some screens. In most cases you should
//    use "ifRoom" instead. Mutually exclusive with "ifRoom" and "never". -->
//            <flag name="always" value="2" />
//            <!-- When this item is shown as an action in the action bar, show a text
//    label with it even if it has an icon representation. -->
//            <flag name="withText" value="4" />
//            <!-- This item's action view collapses to a normal menu
//    item. When expanded, the action view takes over a
//    larger segment of its container. -->
//            <flag name="collapseActionView" value="8" />
//        </attr>
//        <!-- An optional layout to be used as an action view.
//    See {@link android.view.MenuItem#setActionView(android.view.View)}
//             for more info. -->
//        <attr name="actionLayout" format="reference" />
//        <!-- The name of an optional View class to instantiate and use as an
//            action view. See {@link android.view.MenuItem#setActionView(android.view.View)}
//             for more info. -->
//        <attr name="actionViewClass" format="string" />
//        <!-- The name of an optional ActionProvider class to instantiate an action view
//            and perform operations such as default action for that menu item. -->
//        <attr name="actionProviderClass" format="string" />
//    </declare-styleable>
//}
