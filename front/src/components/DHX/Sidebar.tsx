import React, { FC, useRef, useEffect } from "react";
import { Sidebar as SidebarDHX, TreeCollection } from "dhx-suite";
import { Box } from "@chakra-ui/react";
import { relative } from "path";
import { Affix } from "antd";

interface SidebarProps {
	css?: string;
	width?: string | number;
	height?: string,
	minWidth?: string | number;
	collapsed?: boolean;
	data?: Array<any> | TreeCollection;
	onReady: (item: SidebarDHX) => void;
}

const Sidebar: FC<SidebarProps> = (props) => {
	const elRef = useRef<HTMLDivElement>(null);
	let sidebar: SidebarDHX;

	useEffect(() => {
		if (elRef.current) {
			const newSidebar = new SidebarDHX(elRef.current, {
				css: props.css ? props.css : "dhx_widget--bg_white",
				width: props.width,
				height: props.height,
			});
			if (sidebar) {
				sidebar.destructor();
			}
			sidebar = newSidebar;
			// sidebar.collapse();
			if (props.onReady) {
				props.onReady(newSidebar);
			}
		}
	}, [props.css]);

	return (
		<Affix offsetTop={70}>


			<div style={{ height: '100vh', paddingBottom: 70, borderRight: '1px solid #E2E8F0' }}>
				<Box ref={elRef} style={{ height: '100%' }}></Box>
				{/* <div style={{ width: '100%', height: 1, backgroundColor: '#E2E8F0' }}></div> */}
			</div>
		</Affix>
	)

	// <div style={{ height: '100%', borderRight: '1px solid #E2E8F0', borderBottom: '1px solid #E2E8F0' }}>

	// 	<div style={{ height: '100vh', paddingBottom: 60 }}>
	// 		<Box ref={elRef} style={{ height: '100%' }}></Box>
	// 		{/* <div style={{ width: '100%', height: 1, backgroundColor: '#E2E8F0' }}></div> */}
	// 	</div>

	// </div>
	// 	;
}

export default Sidebar;
