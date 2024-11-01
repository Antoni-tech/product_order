import { Button } from 'antd'
import { SearchOutlined } from '@ant-design/icons';

function LinkBtn() {
	return (
		<Button
			shape="circle"
			icon={<SearchOutlined />}
		/>
	)
}

export default LinkBtn