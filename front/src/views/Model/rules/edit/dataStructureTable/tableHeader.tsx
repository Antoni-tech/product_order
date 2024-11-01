import React from 'react'
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

function RulesDataStructureTableHeader() {

	const HeaderStyle = {
		width: '100%',
		paddingRight: '10px',
		height: 30,
		border: '1px solid #E2E8F0',
		backgroundColor: '#fafafa'
	}

	return (
		<Flex style={HeaderStyle} justify='flex-end' align='center'>
			<Row style={{ width: '800px' }}>
				<SingleTitle span={16} title='Name' textAlign='left' justifyContent='flex-start' />
				<SingleTitle span={8} title='test' textAlign='left' justifyContent='flex-start' />
			</Row>
		</Flex>
	)
}

export default RulesDataStructureTableHeader