import React, {FC, useEffect, useState} from "react";
import {Toolbar as ToolbarDHX} from "dhx-suite";
import Toolbar from "./Toolbar";
import {useDispatch} from "react-redux";
import {ComponentType} from "../../common/constants";
import {DHTMLXTypes} from "../../redux/DHTMLX/DHTMLReducer";

interface ToolbarGrid {
    idEvent: string | null;
    item: ComponentType;
    toolbarItemsVisibility?: Array<string>;
}

const ToolbarGridModel: FC<ToolbarGrid> = (props) => {
    const [toolbar, setToolbar] = useState<ToolbarDHX>();
    const dispatch = useDispatch();

    const handleOnclick = (id: string | number) => {
        if (props.idEvent) {
            dispatch({type: DHTMLXTypes.EVENT_TOOLTIP, payload: {componentType: props.item, id: props.idEvent, actions: id}});
        }
    };

    useEffect(() => {
        if (toolbar && props.toolbarItemsVisibility) {
            toolbar.data.forEach((item: any) => {
                if (props.toolbarItemsVisibility?.includes(item.id)) {
                    toolbar.show(item.id);
                } else {
                    toolbar.hide(item.id);
                }
            })
        }
    }, [toolbar, props.toolbarItemsVisibility]);

    useEffect(() => {
        if (toolbar) {
            toolbar.data.add({id: "add", icon: "mdi mdi-plus", tooltip: "Added select item"});
            toolbar.data.add({id: "remove", icon: "mdi mdi-minus", tooltip: "Remove select item"});
            toolbar.data.add({id: "save", icon: "mdi mdi-content-save", tooltip: "Save change"});
            toolbar.data.add({type: "separator"});
            toolbar.data.add({id: "stop", icon: "mdi mdi-stop", tooltip: "Stop"});
            toolbar.data.add({id: "play", icon: "mdi mdi-play", tooltip: "Play"});
            toolbar.data.add({id: "pause", icon: "mdi mdi-pause", tooltip: "Pause"});
            toolbar.events.on('click', (id) => handleOnclick(id));
        }
    }, [toolbar]);


    return <Toolbar onReady={(toolbar) => setToolbar(toolbar)}/>;
};

export default ToolbarGridModel;
