import React, { useState } from 'react';
import { Badge, ConfigProvider, Divider, Menu, MenuProps } from 'antd';
import {
	ContainerOutlined,
	DesktopOutlined,
	PieChartOutlined,
} from '@ant-design/icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHouse, faCodeBranch, faUserGear, faUserPen, faGear, faIndent, faSliders } from '@fortawesome/free-solid-svg-icons';
import { faBell, faClipboard, faComments } from '@fortawesome/free-regular-svg-icons'
import { useNavigate } from 'react-router-dom';
import { URLPaths } from '../../config/application/URLPaths';

type MenuItem = Required<MenuProps>['items'][number];

function getItem(
	label: React.ReactNode,
	key: React.Key,
	icon?: React.ReactNode,
	children?: MenuItem[],
	type?: 'group',
): MenuItem {
	return {
		key,
		icon,
		children,
		label,
		type,
	} as MenuItem;
}

function SiderMenu({ collapsed }: { collapsed: boolean }) {

	const navigate = useNavigate();
	const [selectedKeys, setSelectedKeys] = useState<string[]>(['']);
	const operationIcon = process.env.PUBLIC_URL + '/static/operations.svg';

	const handleMenuClick = (key: React.Key) => {
		setSelectedKeys([key.toString()]);

		if (key === 'models') {
			navigate(URLPaths.MODELS.link);
		}
		else
			navigate("/")
	};

	const items: MenuItem[] = [
		getItem('Домашняя страница', 'home', <FontAwesomeIcon style={{ fontSize: '18px' }} icon={faHouse} />),
		getItem('Операции', 'operations',
			<span style={{ display: 'flex', alignItems: 'center', paddingLeft: 0, width: 18, height: '100%' }}>
				<img src={operationIcon} alt='operations' style={{ width: 18, height: '24px', objectFit: 'cover' }} />
			</span>

		),
		getItem(
			<Badge.Ribbon text={'30'} placement='end' style={{ marginRight: '8px' }}>
				<span>Инциденты и обработка</span>
			</Badge.Ribbon>, 'incidents',
			// <Badge count={30} size='small' color='blue'>
			<FontAwesomeIcon style={{ fontSize: '18px' }} icon={faIndent} />
			// </Badge>
		),

		getItem('Планы и отчетность', 'plan', <FontAwesomeIcon style={{ fontSize: '18px' }} icon={faClipboard} />, [
			getItem('Option 5', '5'),
			getItem('Option 6', '6'),
			getItem('Option 7', '7'),
		]),
		{ type: 'divider' },
		getItem('Управление моделями', 'models', <FontAwesomeIcon style={{ fontSize: '18px' }} icon={faCodeBranch} />),
		{ type: 'divider' },

		getItem('Учетные записи и внешние системы', 'user_settings', <FontAwesomeIcon style={{ fontSize: '18px' }} icon={faUserGear} />),
	];

	const items2: MenuItem[] = [
		getItem('Чат', '11', <FontAwesomeIcon style={{ fontSize: '18px' }} icon={faComments} />),
		getItem('Уведомления', '12',
			<Badge count={24} size='small'>
				<FontAwesomeIcon style={{ fontSize: '18px' }} icon={faBell} />
			</Badge>
		),
		getItem('Настройки', '13', <FontAwesomeIcon style={{ fontSize: '18px' }} icon={faGear} />, [
			getItem('Моя учетная запись', 'my_account', <FontAwesomeIcon icon={faUserPen} />),
			getItem('Общие настройки', 'general_settings', <FontAwesomeIcon icon={faSliders} />),

		]),
		{ type: 'divider' },
	];


	return (

		<>
			<Menu
				defaultSelectedKeys={['home']}
				mode="inline"
				theme="light"
				inlineCollapsed={collapsed}
				onClick={({ key }) => handleMenuClick(key)}
				selectedKeys={selectedKeys}
				items={items}
				style={{ height: '70%', display: 'flex', flexDirection: 'column', justifyContent: 'flex-start' }}
			>
			</Menu>

			<Menu
				selectedKeys={selectedKeys}
				mode="vertical"
				theme="light"
				inlineCollapsed={collapsed}
				onClick={({ key }) => handleMenuClick(key)}
				items={items2}
				style={{ height: '30%', display: 'flex', flexDirection: 'column', justifyContent: 'flex-end' }}
			>
			</Menu>
		</>
	);
}

export default SiderMenu;
