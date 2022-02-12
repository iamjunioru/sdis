import requests

def main():
	print('*---------------------------------*')
	print('|    ==> Consulta de CEP <==      |')
	print('*---------------------------------*')

	cep_input = input('* Digite o CEP para a consulta: \n')

	if len(cep_input) != 8:
		print('[Quantidade de dígitos inválida]')
		exit()

	request = requests.get('https://viacep.com.br/ws/{}/json/'.format(cep_input))

	address_data = request.json()

	if 'erro' not in address_data:
		print('*---------------------------------*')
		print('|    ==> CEP ENCONTRADO <==       |')
		print('*---------------------------------*')
		
		print('CEP: {}'.format(address_data['cep']))
		print('Logradouro: {}'.format(address_data['logradouro']))
		print('Bairro: {}'.format(address_data['bairro']))
		print('Complemento: {}'.format(address_data['complemento']))
		print('DDD: {}'.format(address_data['ddd']))
		print('Gia: {}'.format(address_data['gia']))
		print('IBGE: {}'.format(address_data['ibge']))
		print('Localidade: {}'.format(address_data['localidade']))
		print('Siafi: {}'.format(address_data['siafi']))
		print('UF: {}'.format(address_data['uf']))
		
	else:
		print('{}: CEP inválido.'.format(cep_input))

	print('---------------------------------')
	option = int(input('- Deseja realizar uma nova consulta ?\n1. Sim\n2. Não\n'))
	if option == 1:
		main()
	else:
		print('Saindo...\nSistemas de Informação - SDIS\nRaimundo G. Sousa Jr\n IFCE - Campus Cedro')

if __name__ == '__main__':
	main()
