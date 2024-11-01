import React from 'react';
import Nav from 'react-bootstrap/Nav';
import { Container } from '@chakra-ui/react';
import MegaMenu from "../Customhtml/MegaMenu";
import { useAuth } from "../../hooks/AuthHook";
import { useNavigate } from 'react-router-dom';

const Navsbuttons = () => {
	const { isAuth } = useAuth();
	const navigate = useNavigate();

	return (
		<Container style={{ maxWidth: "none", marginLeft: 15 }}>
			<Nav className="me-auto" style={{ display: "flex", alignItems: "center" }}>
				{!isAuth &&
					<Nav.Link style={{ marginRight: '0px' }}
					// onClick={() => navigate('/registration')}
					>
						Регистрация
					</Nav.Link>
				}
				<MegaMenu />
				<Nav.Link style={{ marginRight: '20px', fontSize: '14px' }} href="#Демо">Демо</Nav.Link>
				<Nav.Link style={{ marginRight: '20px', fontSize: '14px' }} href="#Тарифы">Тарифы</Nav.Link>
				<Nav.Link style={{ marginRight: '20px', fontSize: '14px' }} href="#Поддержка">Поддержка</Nav.Link>
				<Nav.Link style={{ marginRight: '20px', fontSize: '14px' }} href="#Поддержка">Компания</Nav.Link>
			</Nav>
		</Container>
	);
}
export default Navsbuttons;