import React, { FC, useRef, useEffect, useState } from "react";
import { Menu as MenuDHX, TreeCollection } from "dhx-suite";
import PropTypes from 'prop-types';

interface SidebarProps {
	css?: string;
	width?: string | number;
	minWidth?: string | number;
	collapsed?: boolean;
	fontSize?: string | number;
	data?: Array<any> | TreeCollection;
}

const MenuHelpLang: FC<SidebarProps> = (props) => {
	const elRef = useRef<HTMLDivElement>(null);
	let menu: MenuDHX;

	const handleAddToCart = (id: string | number, event: string) => {
		console.log("id :", id, "event:", event)
	};

	useEffect(() => {
		if (elRef.current) {
			const newMenu = new MenuDHX(elRef.current, {
				css: props.css ? props.css : " dhx_widget--bg_white",
				fontSize: props.fontSize ? props.fontSize : "16px",
			});
			newMenu.data.load(`${process.env.PUBLIC_URL}/static/menuHelpLang.json`);
			newMenu.events.on("click", id => handleAddToCart(id, "click"));

			if (menu) {
				menu.destructor();
			}
			menu = newMenu;
		} else {
			console.error("elRef.current is null");
		}
	}, [props.css]);

	return <div ref={elRef}></div>;
};

export default MenuHelpLang;