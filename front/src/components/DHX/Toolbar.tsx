import React, {FC, useRef, useEffect} from "react";
import {Toolbar as ToolbarDHX, TreeCollection} from "dhx-suite";
import {Box} from "@chakra-ui/react";

interface ToolbarProps {
    css?: string;
    width?: string | number;
    minWidth?: string | number;
    collapsed?: boolean;
    data?: Array<any> | TreeCollection;
    onReady: (item: ToolbarDHX) => void;
}

const Toolbar: FC<ToolbarProps> = (props) => {
    const elRef = useRef<HTMLDivElement>(null);
    let toolbar: ToolbarDHX;

    useEffect(() => {
        if (elRef.current) {
            const newMenu = new ToolbarDHX(elRef.current, {
                css: props.css ? props.css : " toolbar_template_a",
            });

            if (toolbar) {
                toolbar.destructor();
            }
            toolbar = newMenu;
            if (props.onReady) {
                props.onReady(newMenu);
            }
        }
    }, [props.css]);

    return <Box ref={elRef}></Box>;
};

export default Toolbar;
