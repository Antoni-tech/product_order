import { Box, Flex, Text, Img, ChakraProps } from "@chakra-ui/react";
import Logo from "../../assets/img/logo.png";
import NonauthAva from "../../assets/img/NonAuthAvatar.png";
import React, { FC, useEffect, useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router";
import { useAuth } from "../../hooks/AuthHook";
import { URLPaths } from "../../config/application/URLPaths";
import { NavBarButton } from "../Buttons/NavBarButton";
import MenuModal from "../Modals/MenuModal";
import { NavLink } from "react-router-dom";
import MenuHelpLang from "../DHX/MenuHelpLang";
import Navsbuttons from "../DHX/Navsbuttons";
import 'flowbite';
import { Menu as MenuDHX } from "dhx-suite";
import { UserController } from "../../controllers/UserController";
import { Affix } from "antd";

export const Navbar: FC = () => {
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const { isAuth } = useAuth();
	const userController = new UserController(dispatch)
	const storedLogin = sessionStorage.getItem('login');
	const capitalizedLogin = storedLogin ? storedLogin.charAt(0).toUpperCase() + storedLogin.slice(1) : '';
	const location = useLocation();
	const [menuDHX, setMenuDHX] = useState<MenuDHX>();
	const [isMenuOpen, setIsMenuOpen] = useState(false);


	useEffect(() => {
		if (storedLogin !== "undefined" && storedLogin !== null) {
			userController.getUser({ loginOrId: storedLogin }).then(() => {
			})
		}
	}, [storedLogin]);

	const handleMenu = (id: string | number, event: string) => {
		console.log("id :", id, "event:", event)
	};

	useEffect(() => {
		if (menuDHX) {
			menuDHX.data.load(`${process.env.PUBLIC_URL}/static/menu.json`);
			menuDHX.events.on("click", id => handleMenu(id, "click"));
		}
	}, [menuDHX]);

	return (
		// <Affix offsetTop={0} >
		<Box style={{ backgroundColor: '#fff', zIndex: 10 }}>
			<Flex
				justifyContent="space-between"
				alignItems="center"
				padding='0 12px'
				height='60px'
				width='100%'
			>

				<Flex >

					<NavBarButton onClick={() => isAuth ? setIsMenuOpen(!isMenuOpen) : navigate(URLPaths.AUTH.link + location.pathname)}
						style={{
							// margin: '-5px auto auto auto',
							height: '100%',
							padding: '5px 0 10px 0',
						}}>
						{isAuth ? (
							<div
								style={{
									width: '100px',
									height: '60px',
									display: 'flex',
									justifyContent: 'center',
									verticalAlign: 'middle',
									flexDirection: 'column',
									alignItems: 'flex-start'
								}}>
								<div
									style={{
										borderRadius: '50%',
										width: '35px',
										height: '35px',
										backgroundColor: 'purple',
										display: 'flex',
										alignItems: 'center',
										justifyContent: 'center',
									}}
								>
									<Text fontSize="18px" color="white">
										{capitalizedLogin.charAt(0)}
									</Text>
								</div>

								<Flex style={{ height: 20 }} align='center'>
									<span style={{ fontWeight: 200, padding: 0, margin: 0, fontSize: 10 }}>{capitalizedLogin}/</span>
									<span style={{ fontWeight: 200, padding: 0, margin: 0, fontSize: 10 }}>Sign out</span>
								</Flex>



							</div>
						) : (
							<div style={{ padding: '5px 0' }}>
								<img className="w-15 h-10 rounded-full"
									src={NonauthAva}
									alt="Not Authenticated, Rounded avatar"
								// style={{ marginBottom: '-10px' }}
								/>
								<div style={{ marginLeft: '-3px', verticalAlign: 'middle' }}>
									<Text fontSize="10px" height={'15px'} >
										Sign In
									</Text>
								</div>
							</div>
						)}
					</NavBarButton>

					<Navsbuttons />

				</Flex>


				<Flex alignItems="center">
					<Box paddingRight={'12px'} >
						<MenuHelpLang />
					</Box>
					<Box ml="auto">
						<NavLink to={URLPaths.HOME.link}>
							<Img src={Logo} alt="logo" outline="none" maxWidth="220px" maxHeight="54px" />
						</NavLink>
					</Box>
				</Flex>
			</Flex>
			{/* <Box borderBottom="1px solid #B3B2B2" width="100%"
					boxShadow={'rgba(0, 0, 0, 0.25) 0px 54px 55px, rgba(0, 0, 0, 0.12) 0px -12px 30px, rgba(0, 0, 0, 0.12) 0px 4px 6px, rgba(0, 0, 0, 0.17) 0px 12px 13px, rgba(0, 0, 0, 0.09) 0px -3px 5px'}
				></Box> */}
			<MenuModal isOpen={isMenuOpen} onClose={() => setIsMenuOpen(false)} buttonPosition={{ top: 0, left: 980 }} />
		</Box>
		// </Affix>
	);
};
