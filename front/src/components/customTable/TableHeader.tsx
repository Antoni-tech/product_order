import { Flex, Row, Col } from 'antd'

const SingleTitle = ({ textAlign, title, span, justifyContent }:
	{ title: string, textAlign?: "left" | "right" | "center", span: number, justifyContent: "flex-start" | "center" }) => {

	const styles = {
		textAlign,
		display: 'flex',
		alignItems: 'center',
		justifyContent
	}

	return (
		<Col span={span} style={styles}>{title}</Col>
	)
}

const HeaderStyle = {
	width: '100%',
	paddingRight: '10px',
	height: 50,
	border: '1px solid #E2E8F0',
	backgroundColor: '#E2E8F0',
	borderRadius: '10px 10px 0px 0px'
}

const TableHeader = () => {
	return (
		<Flex style={HeaderStyle} justify='flex-end' align='center'>
			<Row style={{ width: '930px' }}>
				<SingleTitle span={4} title='Наименование' textAlign='left' justifyContent='flex-start' />
				<SingleTitle span={4} title='Тип данных' textAlign='left' justifyContent='flex-start' />
				<SingleTitle span={4} title='Макс. размер' textAlign='left' justifyContent='flex-start' />
				<SingleTitle span={3} title='Пустые' textAlign='center' justifyContent='center' />
				<SingleTitle span={3} title='Спецсимволы' textAlign='center' justifyContent='center' />
				<SingleTitle span={3} title='Множество' textAlign='center' justifyContent='center' />
				<SingleTitle span={3} title='Макс. кол-во' textAlign='left' justifyContent='flex-start' />
			</Row>
		</Flex>
	)
}

export default TableHeader