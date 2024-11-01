
export const incidentColumns = [
	{
		title: 'Номер',
		dataIndex: 'number',
		key: 'number',
		width: '30%',
	},
	{
		title: 'Текстовое значение',
		dataIndex: 'textValue',
		key: 'textValue',
		width: '40%',
	},
	{
		title: 'Время создания',
		dataIndex: 'createdAt',
		key: 'createdAt',
		width: '40%',
		render: (text: string) => {

			const dateObj = new Date(text);

			const day = dateObj.getDate().toString().padStart(2, "0");
			const month = (dateObj.getMonth() + 1).toString().padStart(2, "0");
			const year = dateObj.getFullYear().toString();
			const hours = dateObj.getHours().toString().padStart(2, "0");
			const minutes = dateObj.getMinutes().toString().padStart(2, "0");
			const seconds = dateObj.getSeconds().toString().padStart(2, "0");

			const formattedDate = `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;

			return (
				<span>{formattedDate}</span>
			)
		},
	}
];