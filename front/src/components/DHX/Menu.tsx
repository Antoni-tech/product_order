import React, {FC, useRef, useEffect, useState} from "react";
import {Menu as MenuDHX, TreeCollection} from "dhx-suite";
import {Box} from "@chakra-ui/react";

interface MenuProps {
    css?: string;
    width?: string | number;
    minWidth?: string | number;
    collapsed?: boolean;
    data?: Array<any> | TreeCollection;
    onReady: (item: MenuDHX) => void;
}

const Menu: FC<MenuProps> = (props) => {
    const elRef = useRef<HTMLDivElement>(null);
    let menu: MenuDHX;

    useEffect(() => {
        if (elRef.current) {
            const newMenu = new MenuDHX(elRef.current, {
                css: props.css ? props.css : " dhx_widget--bg_white",
            });

            // menu.data.getItem("languages")

            if (menu) {
                menu.destructor();
            }
            menu = newMenu;
            if (props.onReady) {
                props.onReady(newMenu);
            }
        }
    }, [props.css]);

    return <Box ref={elRef}></Box>;;
};

export default Menu;
