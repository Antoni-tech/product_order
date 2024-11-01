import { useState } from 'react'
import { Flex, Typography, Button, Input } from 'antd'
import styles from './editModel.module.scss'

function ModelTesting() {

	const [value, setValue] = useState('launching...')
	const [number, setNumber] = useState(0)



	const start = () => {
		setValue('launching...');
		let lines: string[] = [];
		for (let i = 1; i <= 20; i++) {
			setTimeout(() => {
				lines.push(`Launching operation #${i}`);
				setValue(lines.join('\n'));
				setNumber(i * 133)
			}, i * 1000);
		}

	};

	return (



		<Flex style={{ width: '100%', marginTop: 50 }} gap='small' justify='flex-start'>
			<Typography.Title level={5} style={{ width: '25%', marginLeft: 20 }}>Тестирование функционирования</Typography.Title>

			<Flex justify='space-between' style={{ width: '70%' }}>
				<Button onClick={start}>Запуск</Button>

				<Flex vertical gap='small' className={styles.console}>
					<Typography.Text >Отражение результатов тестирования</Typography.Text>
					<Input.TextArea
						value={value}
						placeholder="Controlled autosize"
						autoSize={{ minRows: 5, maxRows: 5 }}
						className={styles.textarea}
						disabled
					/>

				</Flex>
			</Flex>
		</Flex>


	)
}

export default ModelTesting