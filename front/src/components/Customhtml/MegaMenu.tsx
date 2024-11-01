import { useNavigate } from 'react-router-dom';
import 'flowbite';
import Dashboard from "../../assets/img/dashboard.png";
import MyCollapse from "./MyCollapse";

const MegaMenu = () => {
	const navigate = useNavigate();

	return (
		<MyCollapse triggerId={'mega-menu-full-cta'} targetId={'mega-menu-full-cta-dropdown'}>
			<div className="flex flex-wrap justify-between items-center mx-auto max-w-screen-xl py-4 pr-4">
				<div id="mega-menu-full-cta"
					className="items-center justify-between hidden w-full md:flex md:w-auto md:order-1">
					<ul className="   flex flex-col mt-4 font-medium md:flex-row md:mt-0 md:space-x-8
                        rtl:space-x-reverse">
						<li>
							<button id="mega-menu-full-cta"
								className="flex items-center  justify-between w-full py-2 px-3 font-medium w-2.5
                                        h-2.5 ms-3 text-gray-900 border-b border-gray-100 md:w-auto hover:bg-gray-50
                                        md:hover:bg-transparent md:border-0 md:hover:text-blue-600
                                        md:p-0 dark:text-white md:dark:hover:text-blue-500 dark:hover:bg-gray-700
                                        dark:hover:text-blue-500 md:dark:hover:bg-transparent dark:border-gray-700"
								style={{
									fontFamily: "Roboto, Arial, Tahoma, Verdana, sans-serif",
									fontWeight: 400,
									// fontSize: "calc(3.5px * 4)",
									whiteSpace: 'nowrap',
									fontSize: '14px'
								}}
							>
								Продукты и услуги
								<svg
									className="w-2.5 h-2.5 ml-1.5"
									aria-hidden="true" fill="none"
									viewBox="0 0 10 6"
								>
									<path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round"
										strokeWidth="2" d="m1 1 4 4 4-4" />
								</svg>
							</button>
						</li>
					</ul>
				</div>
			</div>
			<div id="mega-menu-full-cta-dropdown" className=" z-30 box-border h-96 hidden w-100% absolute z-10
                -inset-x-0 ml-2 resize-y mt-0 grid shadow-md shadow-gray-400 bg-white">
				<div className="grid gap-10 max-w-screen-xl px-5 py-2 mx-auto text-sm text-gray-500 dark:text-gray-400
                md:grid-cols-4 lg:px-0">
					<div className="column">
						<h2 className="text-lg font-bold mb-3"> Column 1 Title</h2>
						<ul className="space-y-2 sm:mb-4 md:mb-1 flex-1 col-span-1"
							aria-labelledby="mega-menu-full-cta-button">
							<li>
								<a href="#" className="flex items-center block p-3 rounded-lg hover:bg-gray-50
                                dark:hover:bg-gray-700 hover:text-blue-600">
									<svg className="w-8 h-8 me-2 text-gray-400 dark:text-gray-500
                                    group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 20 20">
										<path d="M19 4h-1a1 1 0 1 0 0 2v11a1 1 0 0 1-2 0V2a2 2 0 0 0-2-2H2a2 2 0 0 0-2
                                            2v15a3 3 0 0 0 3 3h14a3 3 0 0 0 3-3V5a1 1 0 0 0-1-1ZM3 4a1 1 0 0 1 1-1h3a1
                                            1 0 0 1 1 1v3a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V4Zm9 13H4a1 1 0 0 1 0-2h8a1 1 0
                                            0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 1
                                            1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1 1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1
                                            1 0 2Z"/>
										<path d="M6 5H5v1h1V5Z" />
									</svg>
									<div className="font-semibold">Online Stores</div>
									<span className="text-sm text-gray-500 dark:text-gray-400">
										Connect with third-party tools that you're already using.
									</span>
								</a>
							</li>
							<li>
								<a href="#" className="flex items-center block p-3 rounded-lg hover:bg-gray-50
                                dark:hover:bg-gray-700 hover:text-blue-600">
									<svg className="w-8 h-8 me-2 text-gray-400 dark:text-gray-500
                                    group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 20 20">
										<path
											d="M19 4h-1a1 1 0 1 0 0 2v11a1 1 0 0 1-2 0V2a2 2 0 0 0-2-2H2a2 2 0 0 0-2
                                            2v15a3 3 0 0 0 3 3h14a3 3 0 0 0 3-3V5a1 1 0 0 0-1-1ZM3 4a1 1 0 0 1 1-1h3a1
                                            1 0 0 1 1 1v3a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V4Zm9 13H4a1 1 0 0 1 0-2h8a1 1 0
                                            0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 1
                                            1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1 1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1
                                            1 0 2Z"/>
										<path d="M6 5H5v1h1V5Z" />
									</svg>
									<div className="font-semibold">Online Stores</div>
									<span className="text-sm text-gray-500 dark:text-gray-400">
										Connect with third-party tools that you're already using.
									</span>
								</a>
							</li>
							<li>
								<a href="#" className="flex items-center block p-3 rounded-lg hover:bg-gray-50
                                dark:hover:bg-gray-700 hover:text-blue-600">
									<svg className="w-8 h-8 me-2 text-gray-400 dark:text-gray-500
                                    group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 20 20">
										<path
											d="M19 4h-1a1 1 0 1 0 0 2v11a1 1 0 0 1-2 0V2a2 2 0 0 0-2-2H2a2 2 0 0 0-2
                                            2v15a3 3 0 0 0 3 3h14a3 3 0 0 0 3-3V5a1 1 0 0 0-1-1ZM3 4a1 1 0 0 1 1-1h3a1
                                            1 0 0 1 1 1v3a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V4Zm9 13H4a1 1 0 0 1 0-2h8a1 1 0
                                            0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 1
                                            1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1 1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1
                                            1 0 2Z"/>
										<path d="M6 5H5v1h1V5Z" />
									</svg>
									<div className="font-semibold">Online Stores</div>
									<span className="text-sm text-gray-500 dark:text-gray-400">
										Connect with third-party tools that you're already using.
									</span>
								</a>
							</li>
							<li>
								<a href="#" className="flex items-center block p-3 rounded-lg hover:bg-gray-50
                                dark:hover:bg-gray-700 hover:text-blue-600">
									<svg className="w-8 h-8 me-2 text-gray-400 dark:text-gray-500
                                    group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 20 20">
										<path
											d="M19 4h-1a1 1 0 1 0 0 2v11a1 1 0 0 1-2 0V2a2 2 0 0 0-2-2H2a2 2 0 0 0-2
                                            2v15a3 3 0 0 0 3 3h14a3 3 0 0 0 3-3V5a1 1 0 0 0-1-1ZM3 4a1 1 0 0 1 1-1h3a1
                                            1 0 0 1 1 1v3a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V4Zm9 13H4a1 1 0 0 1 0-2h8a1 1 0
                                            0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 0 1 0 2Zm0-3H4a1 1 0 0 1 0-2h8a1 1 0 1
                                            1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1 1 0 2Zm0-3h-2a1 1 0 0 1 0-2h2a1 1 0 1
                                            1 0 2Z"/>
										<path d="M6 5H5v1h1V5Z" />
									</svg>
									<div className="font-semibold">Online Stores</div>
									<span className="text-sm text-gray-500 dark:text-gray-400">
										Connect with third-party tools that you're already using.
									</span>
								</a>
							</li>
						</ul>
					</div>
					<div className="column" style={{ marginLeft: '4rem', marginRight: '1rem' }}>
						<h2 className="text-lg font-bold mb-3"> Column 2 Title</h2>
						<ul className="hidden mb-1 space-y-2 md:mb-0 md:block">
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
						</ul>
					</div>
					<div className="column" style={{ marginLeft: '1rem', marginRight: '-1rem' }}>
						<h2 className="text-lg font-bold mb-3"> Column 3 Title</h2>
						<ul className="hidden mb-1 space-y-2 md:mb-0 md:block">
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
							<li>
								<a href="#"
									className="text-lg flex items-center text-gray-500 dark:text-gray-400
                                   hover:text-blue-600 dark:hover:text-blue-500 group">
									<span className="sr-only">About us</span>
									<svg
										className="w-8 h-8 me-0 text-gray-400 dark:text-gray-500
                                        group-hover:text-blue-600 dark:group-hover:text-blue-500" aria-hidden="true"
										fill="currentColor" viewBox="0 0 30 20">
										<path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1
                                        1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1
                                        1 1v4h1a1 1 0 0 1 0 2Z"/>
									</svg>
									About Us
								</a>
							</li>
						</ul>
					</div>
					<div className="mt-4 md:mt-0 flex">
						<a href="#"
							className="p-8 bg-local bg-gray-500 bg-center bg-no-repeat bg-cover rounded-lg
                           bg-blend-multiply hover:bg-blend-soft-light dark:hover:bg-blend-darken"
							style={{ backgroundImage: `url(${Dashboard})` }}>
							<p className="max-w-xl mb-5 font-extrabold leading-tight tracking-tight text-white">
								Preview the new Flowbite dashboard navigation.
							</p>
							<button type="button" className="inline-flex items-center px-2.5 py-1.5 text-xs
                            font-medium text-center text-white border border-white rounded-lg hover:bg-white
                            hover:text-gray-900 focus:ring-4 focus:outline-none focus:ring-gray-700">
								Get started
								<svg className="w-3 h-3 ms-2 rtl:rotate-180" aria-hidden="true" fill="none"
									viewBox="0 0 14 10">
									<path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round"
										strokeWidth="2" d="M1 5h12m0 0L9 1m4 4L9 9" />
								</svg>
							</button>
						</a>
					</div>
					<ul className="mt-[-20px]">
						<li>
							<a href="#"
								className="text-lg flex items-center text-gray-500 dark:text-gray-400 hover:text-blue-600
                               dark:hover:text-blue-500 group">
								<span className="sr-only">About us</span>
								<svg
									className="w-6 h-6 me-2 text-gray-400 dark:text-gray-500 group-hover:text-blue-600
                                    dark:group-hover:text-blue-500" aria-hidden="true" fill="none" viewBox="0 0 14 10">
									<path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round"
										strokeWidth="2" d="M1 5h12m0 0L9 1m4 4L9 9" />
								</svg>
								View all products
							</a>
						</li>
					</ul>
				</div>
			</div>
		</MyCollapse>
	);
}
export default MegaMenu;