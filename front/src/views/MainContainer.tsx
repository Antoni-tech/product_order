import React, { FC, useEffect, useState } from "react";
import { ChakraProps, Flex } from "@chakra-ui/react";
import { useDispatch } from "react-redux";
import { Route, Routes, useNavigate } from "react-router";
import { Navbar } from "../components/Navbars/Navbar";
import { URLPaths } from "../config/application/URLPaths";
import { AuthorizationController } from "../controllers/AuthController";
import { Page404 } from "./404/Page404";
import { AuthorizationForm } from "./Forms/Authoization/AuthorizationForm";
import Sidebar from "../components/DHX/Sidebar";
import { Home } from "./Home/Home";
import { usePrivileges } from "../hooks/PrivilegesProvider";
import { Sidebar as SidebarDHX } from "dhx-suite";

import RegForm from "../components/Customhtml/RegForm";
import ModelsManage from "./Model/common/all/ModelsManage";
import { FormModels } from "./Model/common/edit/FormModels";

import RuleDefaultForm from "./Model/rules/edit";
import DataStructureForm from "./Model/dataStructures/edit/DataStructureForm";
import ConnectorFormEdit from "./Model/connectors/edit/ConnectorFormEdit";

import { Layout, Menu } from 'antd';
import styles from './MainContainer.module.scss'
import SiderMenu from "../components/SiderMenu/SiderMenu";
interface MainContainerProps {
	isAuth: boolean;
}

export const MainContainer: FC<MainContainerProps> = ({ isAuth }) => {
	const { priv } = usePrivileges()
	const dispatch = useDispatch()
	const authController = new AuthorizationController(dispatch)
	const navigate = useNavigate();
	const [sidebarDHX, setSidebarDHX] = useState<SidebarDHX>();
	const [collapsed, setCollapsed] = useState(false);
	// const [toolbarDHX, setToolbarDHX] = useState<ToolbarDHX>();
	const containerStyleConfig: ChakraProps = {
		minH: "90%",
		bg: "#fff",
		overflowX: "hidden"
	}

	const siderStyle: React.CSSProperties = {
		textAlign: 'center',
		lineHeight: '120px',
		color: '#fff',
		backgroundColor: '#1677ff',
		height: '100vh', position: 'fixed', left: 0, top: 70, bottom: 0
	};

	useEffect(() => {
		let intervalId: NodeJS.Timeout;

		const refreshAndSetTimer = async () => {
			try {
				if (isAuth) {
					// Сразу после обновления страницы
					await authController.refresh();
					sessionStorage.setItem('lastRefreshTime', new Date().getTime().toString());
					// Очищаем предыдущий таймер, если он существует
					clearInterval(intervalId);

					// Запускаем новый таймер на 3 минуты
					intervalId = setInterval(async () => {
						await refreshAndSetTimer();
					}, 180000); // 3 минуты в миллисекундах
				}
			} catch (error) {
				console.error('Error refreshing token:', error);
			}
		};

		// При монтировании компонента и наличии авторизации запускаем первый таймер
		if (isAuth) {
			refreshAndSetTimer();
		}

		// При размонтировании компонента очищаем таймер
		return () => {
			clearInterval(intervalId);
		};
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [isAuth])

	const handleSidebarDHX = (id: string | number, event: string) => {
		if (sidebarDHX) {
			if (id === "connectors") {
				navigate(`${URLPaths.IN_CONNECTORS.link}`)
			} else if (id === "models") {
				navigate(URLPaths.MODELS.link);
			}
			if (id === "toggle") {
				const toggleItem = sidebarDHX.data.getItem("toggle");
				sidebarDHX.toggle();
				if (sidebarDHX.config.collapsed) {
					toggleItem.icon = "mdi mdi-menu";
				} else {
					toggleItem.icon = "mdi mdi-backburger";
				}
			}
		}
	}

	useEffect(() => {
		if (sidebarDHX) {
			sidebarDHX.data.load(`${process.env.PUBLIC_URL}/static/sidebar.json`);
			sidebarDHX.events.on("click", id => handleSidebarDHX(id, "click"));
		}
	}, [sidebarDHX]);

	const { Header, Sider, Content } = Layout;

	return (
		// <>
		// 	<Flex {...containerStyleConfig}>
		// 		<Flex minW="100%" mx="auto" h="auto" minH="100vh" flexDir="column">
		// 			<Navbar />
		// 			<Flex flexDir="row" minW="98%" minH="100vh" height='100%' mr={"auto"}>
		// 				{isAuth && (
		// 					<Sidebar width={300} height="100%" onReady={(sidebar) => setSidebarDHX(sidebar)} />
		// 				)}
		// 				<Routes>
		// 					{/*404*/}
		// 					<Route path={"*" || "/404"} element={<Page404 />} />
		// 					<Route path={URLPaths.AUTH.link + "/*"} element={<AuthorizationForm />} />
		// 					<Route path={URLPaths.HOME.link} element={<Home />} />
		// 					{/* {(priv?.USER_VIEW) && */}
		// 					<Route path={URLPaths.MODELS.link} element={<ModelsManage />} />
		// 					{/* } */}
		// 					{/* {(priv?.USER_CREATE) && */}
		// 					<>
		// 						<Route path={URLPaths.RULES_CREATE.link} element={<RuleDefaultForm edit={false} />} />
		// 						<Route path={URLPaths.MODEL_CREATE.link} element={<FormModels edit={false} />} />
		// 					</>
		// 					{/* // } */}
		// 					{/* {(priv?.USER_EDIT) && */}
		// 					<>
		// 						{/* <Route path={URLPaths.IN_CONNECTOR_EDIT.link + "/:id"} element={<FormInput edit={true} />} /> */}
		// 						<Route path={URLPaths.IN_CONNECTOR_EDIT.link + "/:id"} element={<ConnectorFormEdit edit={true} connectorType="input" />} />
		// 						<Route path={URLPaths.IN_CONNECTOR_EDIT.link + "/:id" + "/:fromModel"} element={<ConnectorFormEdit edit={true} connectorType="input" />} />
		// 						<Route path={URLPaths.OUT_CONNECTOR_EDIT.link + "/:id"} element={<ConnectorFormEdit edit={true} connectorType="output" />} />
		// 						<Route path={URLPaths.OUT_CONNECTOR_EDIT.link + "/:id" + "/:fromModel"} element={<ConnectorFormEdit edit={true} connectorType="output" />} />

		// 						<Route path={URLPaths.RULES_EDIT.link + "/:id"} element={<RuleDefaultForm edit={true} />} />
		// 						{/* <Route path={URLPaths.RULES_EDIT.link + "/:id"} element={<FormRule edit={true} />} /> */}
		// 						<Route path={URLPaths.RULES_EDIT.link + "/:id" + "/:fromModel"} element={<RuleDefaultForm edit={true} />} />
		// 						<Route path={URLPaths.MODEL_EDIT.link + "/:id"} element={<FormModels edit={true} />} />
		// 						<Route path={URLPaths.DATASTRUCTURE_EDIT.link + "/:id"} element={<DataStructureForm edit={true} />} />

		// 					</>
		// 					{/* } */}
		// 					<Route path={URLPaths.REGFORM.link} element={<RegForm />} />
		// 				</Routes>
		// 			</Flex>
		// 		</Flex>
		// 	</Flex>
		// </>

		<Layout style={{ backgroundColor: '#fff', minHeight: '100vh' }}>

			<Header className={styles.header} >
				<Navbar />
			</Header>

			<Layout hasSider style={{ position: 'relative', minHeight: '100vh' }}>
				{isAuth && (
					<Sider
						theme='light'
						width={300}
						collapsible
						collapsed={collapsed}
						onCollapse={() => setCollapsed(!collapsed)}
						className={styles.sider}
						style={{
							position: "sticky",
							overflow: "auto",
							top: 70, // Adjust this value based on your Header's height
							height: 'calc(100vh - 75px)', // Adjust this value to fill the remaining height
							left: 0,
						}}
					>

						<SiderMenu collapsed={collapsed} />
					</Sider>
				)}

				<Layout>

					<Content style={{ borderLeft: '1px solid #B3B2B2' }}>
						<Flex {...containerStyleConfig}>
							<Flex minW="100%" mx="auto" h="auto" minH="100vh" flexDir="column">
								{/* <Navbar /> */}
								<Flex flexDir="row" minW="98%" minH="100vh" height='100%' mr={"auto"}>
									{/* {isAuth && (
										<Sidebar width={300} height="100%" onReady={(sidebar) => setSidebarDHX(sidebar)} />
									)} */}
									<Routes>
										{/*404*/}
										<Route path={"*" || "/404"} element={<Page404 />} />
										<Route path={URLPaths.AUTH.link + "/*"} element={<AuthorizationForm />} />
										<Route path={URLPaths.HOME.link} element={<Home isAuth={isAuth} />} />
										{/* {(priv?.USER_VIEW) && */}
										<Route path={URLPaths.MODELS.link} element={<ModelsManage />} />
										{/* } */}
										{/* {(priv?.USER_CREATE) && */}
										<>
											<Route path={URLPaths.RULES_CREATE.link} element={<RuleDefaultForm edit={false} />} />
											<Route path={URLPaths.MODEL_CREATE.link} element={<FormModels edit={false} />} />
										</>
										{/* // } */}
										{/* {(priv?.USER_EDIT) && */}
										<>
											{/* <Route path={URLPaths.IN_CONNECTOR_EDIT.link + "/:id"} element={<FormInput edit={true} />} /> */}
											<Route path={URLPaths.IN_CONNECTOR_EDIT.link + "/:id"} element={<ConnectorFormEdit edit={true} connectorType="input" />} />
											<Route path={URLPaths.IN_CONNECTOR_EDIT.link + "/:id" + "/:fromModel"} element={<ConnectorFormEdit edit={true} connectorType="input" />} />
											<Route path={URLPaths.OUT_CONNECTOR_EDIT.link + "/:id"} element={<ConnectorFormEdit edit={true} connectorType="output" />} />
											<Route path={URLPaths.OUT_CONNECTOR_EDIT.link + "/:id" + "/:fromModel"} element={<ConnectorFormEdit edit={true} connectorType="output" />} />

											<Route path={URLPaths.RULES_EDIT.link + "/:id"} element={<RuleDefaultForm edit={true} />} />
											{/* <Route path={URLPaths.RULES_EDIT.link + "/:id"} element={<FormRule edit={true} />} /> */}
											<Route path={URLPaths.RULES_EDIT.link + "/:id" + "/:fromModel"} element={<RuleDefaultForm edit={true} />} />
											<Route path={URLPaths.MODEL_EDIT.link + "/:id"} element={<FormModels edit={true} />} />
											<Route path={URLPaths.DATASTRUCTURE_EDIT.link + "/:id"} element={<DataStructureForm edit={true} />} />

										</>
										{/* } */}
										<Route path={URLPaths.REGFORM.link} element={<RegForm />} />
									</Routes>
								</Flex>
							</Flex>
						</Flex>
					</Content>
				</Layout>
			</Layout>

		</Layout>
	)
}


